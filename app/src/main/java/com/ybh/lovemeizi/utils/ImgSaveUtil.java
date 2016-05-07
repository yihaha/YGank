package com.ybh.lovemeizi.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 参考: http://stormzhang.com/android/2014/07/24/android-save-image-to-gallery/
 * Created by y on 2016/5/7.
 */
public class ImgSaveUtil {

    public static Observable<Uri> saveImg(final Context context, final String url, final String mkdirName) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap mbitmap = null;
                try {
                    mbitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if (mbitmap == null) {
                    subscriber.onError(new Exception("无法得到图片!"));
                }
                subscriber.onNext(mbitmap);
                subscriber.onCompleted();
            }
        }).map(new Func1<Bitmap, Uri>() {

            @Override
            public Uri call(Bitmap bitmap) {
                File imgDir = new File(Environment.getExternalStorageDirectory(), mkdirName);
                if (!imgDir.exists()) {
                    imgDir.mkdir();
                }

                String fileNme = url.substring(url.lastIndexOf("/")+1);
//                String fileNme = System.currentTimeMillis() + ".jpg";
                File file = new File(imgDir, fileNme);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                        , uri));
                return uri;
            }
        }) .subscribeOn(Schedulers.io());
    }


    public static Observable<Uri> saveImg1(final Context context, final String url, final String mkdirName) {
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                Bitmap mbitmap = null;
                try {
                    mbitmap = Picasso.with(context).load(url).get();
                } catch (IOException e) {
                    subscriber.onError(e);
                }
                if (mbitmap == null) {
                    subscriber.onError(new Exception("无法得到图片!"));
                }
                subscriber.onNext(mbitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                File imgDir = new File(Environment.getExternalStorageDirectory(), mkdirName);
                if (!imgDir.exists()) {
                    imgDir.mkdir();
                }

                String fileNme = url.substring(url.lastIndexOf("/")+1);
//                String fileNme = System.currentTimeMillis() + ".jpg";
                File file = new File(imgDir, fileNme);
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Uri uri = Uri.fromFile(file);
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE
                        , uri));
                return Observable.just(uri);
            }
        }) .subscribeOn(Schedulers.io());
    }


}
