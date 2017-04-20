package com.example.avinash.nittcart.tab_and_bottombar;

import java.io.File;

/**
 * Created by AVINASH on 4/2/2017.
 */
abstract class AlbumStorageDirFactory {
    public abstract File getAlbumStorageDir(String albumName);
}