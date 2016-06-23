package com.tcetra.smartimagecompresser.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;

import rx.Observable;
import rx.functions.Func0;

/**
 * Created on : June 23, 2016
 * Author     : Akshay
 * Email      : akshayt.android@gmail.com
 * GitHub     : https://github.com/akshaytandroid
 */

public class ImageCompressor {
    private static ImageCompressor INSTANCE;
    private Context context;
    //max width and height values of the compressed image is taken as 612x816
    private float maxWidth = 612.0f;
    private float maxHeight = 816.0f;
    private Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;
    private int quality = 80;
    private String destinationDirectoryPath;

    private ImageCompressor(Context context) {
        this.context = context;
        destinationDirectoryPath = context.getCacheDir().getPath() + File.pathSeparator + FileUtil.FILES_PATH;
    }

    public static ImageCompressor getDefault(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ImageCompressor(context);
        }
        return INSTANCE;
    }

    public File compressToFile(File file) {
        return ImageUtil.compressImage(context, Uri.fromFile(file), maxWidth, maxHeight, compressFormat, quality, destinationDirectoryPath);
    }

    public Bitmap compressToBitmap(File file) {
        return ImageUtil.getScaledBitmap(context, Uri.fromFile(file), maxWidth, maxHeight);
    }

    public Observable<File> compressToFileAsObservable(final File file) {
        return Observable.defer(new Func0<Observable<File>>() {
            @Override
            public Observable<File> call() {
                return Observable.just(compressToFile(file));
            }
        });
    }

    public Observable<Bitmap> compressToBitmapAsObservable(final File file) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                return Observable.just(compressToBitmap(file));
            }
        });
    }

    public static class Builder {
        private ImageCompressor imageCompressor;

        public Builder(Context context) {
            imageCompressor = new ImageCompressor(context);
        }

        public Builder setMaxWidth(float maxWidth) {
            imageCompressor.maxWidth = maxWidth;
            return this;
        }

        public Builder setMaxHeight(float maxHeight) {
            imageCompressor.maxHeight = maxHeight;
            return this;
        }

        public Builder setCompressFormat(Bitmap.CompressFormat compressFormat) {
            imageCompressor.compressFormat = compressFormat;
            return this;
        }

        public Builder setQuality(int quality) {
            imageCompressor.quality = quality;
            return this;
        }

        public Builder setDestinationDirectoryPath(String destinationDirectoryPath) {
            imageCompressor.destinationDirectoryPath = destinationDirectoryPath;
            return this;
        }

        public ImageCompressor build() {
            return imageCompressor;
        }
    }
}
