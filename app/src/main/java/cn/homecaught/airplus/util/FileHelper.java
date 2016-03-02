package cn.homecaught.airplus.util;

import android.graphics.Bitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileHelper {
	

	public static FileOutputStream createFileOutputStream(String strPath)
			throws Exception {
		final File file = new File(strPath);
		try {
			return new FileOutputStream(file);
		} catch (Exception e) {
			final File fileParent = file.getParentFile();
			if (!fileParent.exists()) {
				if (fileParent.mkdirs()) {
					return new FileOutputStream(file);
				}
			}
		}

		return null;
	}

	public static void deleteFile(String strPath) {
		File file = new File(strPath);
		file.delete();
	}

	/**
	 * ɾ���ļ���������ļ�����ɾ������ļ���
	 * 
	 * @param strPath
	 */
	public static void deleteFolder(String strPath) {
		File file = new File(strPath);
		if (file.isDirectory()) {
			File fileChilds[] = file.listFiles();
			if (fileChilds == null) {
				file.delete();
				return;
			}
			final int nLength = fileChilds.length;
			if (nLength > 0) {
				for (File fileChild : fileChilds) {
					if (fileChild.isDirectory()) {
						deleteFolder(fileChild.getAbsolutePath());
					} else {
						fileChild.delete();
					}
				}
				file.delete();
			} else {
				file.delete();
			}
		} else {
			file.delete();
		}
	}

	/**
	 * ����bitmap���ļ���
	 * @param strPathDst �ļ�·��
	 * @param bmp bitmapʵ��
	 */
	public static void saveBitmapToFile(String strPathDst, Bitmap bmp) {
		try {
			FileOutputStream fos = createFileOutputStream(strPathDst);
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �����ļ�
	 * @param strPathDst Ŀ���ļ�·��
	 * @param strPathSrc Դ�ļ�·��
	 */
	public static void copyFile(String strPathDst, String strPathSrc) {
		FileOutputStream fos = null;
		FileInputStream fis = null;
		try {
			fos = createFileOutputStream(strPathDst);
			fis = new FileInputStream(strPathSrc);
			byte buf[] = new byte[1024];
			int nReadBytes = 0;
			while ((nReadBytes = fis.read(buf, 0, buf.length)) != -1) {
				fos.write(buf, 0, nReadBytes);
			}
			fos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��ȡ�ļ���ת��Ϊstring
	 * @param strFilePath �ļ�·��
	 * @return
	 */
	public static String readFileToString(String strFilePath) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(
					strFilePath), "GBK"));
			final StringBuffer sb = new StringBuffer();
			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				sb.append(strLine);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	
	
}
