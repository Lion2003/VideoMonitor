package com.yxiaolv.camerasample;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

public class ImageTool {
	/**
	 * �и�ͼƬ
	 * 
	 * @param bmp
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	public static Bitmap imageCut(Bitmap bmp, int left, int top, int right,
			int bottom) {
		Bitmap resultBmp = null;
		int resultWidth = right - left + 1;
		int resultHeight = bottom - top + 1;
		int colors[] = new int[resultWidth * resultHeight];
		bmp.getPixels(colors, 0, resultWidth, left, top, resultWidth,
				resultHeight);
		if (bmp.isRecycled())
			bmp.recycle();
		resultBmp = Bitmap.createBitmap(colors, resultWidth, resultHeight,
				Bitmap.Config.ARGB_8888);
		return resultBmp;
	}

	/**
	 * ��ͼƬ���̶�������С
	 * 
	 * @param bmp
	 * @param iWidth
	 * @param iHeight
	 * @return
	 */
	public static Bitmap imageZoom(Bitmap bmp, int iWidth, int iHeight) {
		Bitmap newBmp = null;
		int imageHeight = bmp.getHeight();
		int imageWidth = bmp.getWidth();
		float scaleW = 1;
		float scaleH = 1;
		double scalex = (float) iWidth / imageWidth;
		double scaley = (float) iHeight / imageHeight;
		scaleW = (float) (scaleW * scalex);
		scaleH = (float) (scaleH * scaley);
		Matrix matrix = new Matrix();
		matrix.postScale(scaleW, scaleH);
		newBmp = Bitmap.createBitmap(bmp, 0, 0, imageWidth, imageHeight,
				matrix, true);
		if (!bmp.isRecycled())
			bmp.recycle();
		return newBmp;
	}

	/**
	 * �ϲ�ͼƬ
	 * 
	 * @param bmps
	 * @return
	 */
	public static Bitmap imageMerge(Bitmap[] bmps) {
		Bitmap resultBmp = null;
		// �ж�bmps����û��ֵΪnull��bmp���еĻ������ʼ��Ϊһ��960*80�Ŀհ�ͼƬ
		int bmp_null[] = new int[960 * 80];
		for (int i = 0; i < 960 * 80; i++)
			bmp_null[i] = 0xffffffff;
		for (int i = 0; i < bmps.length; i++) {
			if (bmps[i] == null) {
				bmps[i] = Bitmap.createBitmap(960, 80, Bitmap.Config.ARGB_8888);
				bmps[i].setPixels(bmp_null, 0, 960, 0, 0, 960, 80);
			}
		}

		// ��ͬͼƬ֮����һ�����Ϊ5���صĺ��߸���
		int devideLineHeight = 5;
		int resultWidth = 0; // �ϲ����ͼ��Ŀ�ȣ�
		int resultHeight = 0; // �ϲ����ͼ��ĸ߶ȣ�
		for (int i = 0; i < bmps.length; i++) {
			if (i != (bmps.length - 1)) {
				if (bmps[i].getWidth() > resultWidth)
					resultWidth = bmps[i].getWidth();
				resultHeight = resultHeight + bmps[i].getHeight()
						+ devideLineHeight;
			} else {
				if (bmps[i].getWidth() > resultWidth)
					resultWidth = bmps[i].getWidth();
				resultHeight = resultHeight + bmps[i].getHeight();
			}
		}
		resultWidth = 960; // ��ʱ����ȶ���Ϊ960
		Log.i("Width & Height", "******* " + resultWidth + " * " + resultHeight
				+ " ********");
		// �����ͼƬ֮��ָ��ߵ���������
		int devideLine[] = new int[resultWidth * devideLineHeight];
		for (int i = 0; i < resultWidth * devideLineHeight; i++)
			devideLine[i] = 0xffff0000;
		// ������һ��resultWidth*resultHeight�ĺ�ɫͼƬ
		resultBmp = Bitmap.createBitmap(resultWidth, resultHeight,
				Bitmap.Config.ARGB_8888);
		// Ϊ��ʶ�𷽱㣬�����ź�ɫͼƬ��Ϊ����ɫ
		int originalColors[] = new int[resultWidth * resultHeight];
		for (int i = 0; i < resultWidth * resultHeight; i++)
			originalColors[i] = 0xffffffff;
		resultBmp.setPixels(originalColors, 0, resultWidth, 0, 0, resultWidth,
				resultHeight); // һ�Ŵ���ɫ��ͼƬ
		// �����ѭ����ͼƬ�����и���ͼƬ���������鼰�ָ��ߵ����������������ͼƬ
		int y = 0;
		int len = bmps.length;
		for (int i = 0; i < len; i++) {
			int iWidth = bmps[i].getWidth();
			int iHeight = bmps[i].getHeight();
			int colors[] = new int[iWidth * iHeight];
			bmps[i].getPixels(colors, 0, iWidth, 0, 0, iWidth, iHeight);
			if (i != (len - 1)) {
				resultBmp.setPixels(colors, 0, iWidth, 0, y, iWidth, iHeight);
				y = y + iHeight;
				resultBmp.setPixels(devideLine, 0, resultWidth, 0, y,
						resultWidth, devideLineHeight);
				y = y + devideLineHeight;
			} else {
				resultBmp.setPixels(colors, 0, iWidth, 0, y, iWidth, iHeight);
			}
			if (!bmps[i].isRecycled())
				bmps[i].recycle();
		}
		return resultBmp;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param bmp
	 * @param path
	 * @param filename
	 * @param quality
	 */
	public static File saveImage(Bitmap bmp, String path, String filename,
			int quality) {
		String time = callTime();
		File imageFile = null;
		if (bmp != null) {
			try {
				/* �ļ������ھʹ��� */
				File dirFile = new File(
						Environment.getExternalStorageDirectory(), path);
				if (!dirFile.exists()) {
					dirFile.mkdir();
				}

				/* ������Ƭ�ļ� */
				imageFile = new File(dirFile, filename + time + ".jpg");
				FileOutputStream bos = new FileOutputStream(
						imageFile.getAbsolutePath());
				bmp.compress(Bitmap.CompressFormat.JPEG, quality, bos);
				bos.flush();
				bos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!bmp.isRecycled())
			bmp.recycle();
		return imageFile;
	}

	/**
	 * ��ȡϵͳʱ��
	 * 
	 * @return
	 */
	public static String callTime() {
		long backTime = new Date().getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date(backTime));
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DAY_OF_MONTH);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int second = cal.get(Calendar.SECOND);
		String time = "" + year + month + date + hour + minute + second;
		return time;
	}
}
