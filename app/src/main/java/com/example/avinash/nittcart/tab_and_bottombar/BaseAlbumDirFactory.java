package com.example.avinash.nittcart.tab_and_bottombar;

import android.os.Environment;

import java.io.File;

/**
 * Created by AVINASH on 4/2/2017.
 */
public class BaseAlbumDirFactory extends AlbumStorageDirFactory {
    private static final String CAMERA_DIR = "/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File(
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}
