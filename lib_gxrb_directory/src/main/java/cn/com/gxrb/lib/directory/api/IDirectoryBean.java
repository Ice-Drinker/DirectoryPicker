package cn.com.gxrb.lib.directory.api;

import java.util.ArrayList;

public interface IDirectoryBean {
    int getId();

    String getName();

    boolean isFolder();

    ArrayList<? extends IDirectoryBean> getDirectories();
}
