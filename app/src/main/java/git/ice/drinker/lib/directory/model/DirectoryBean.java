package git.ice.drinker.lib.directory.model;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import cn.com.gxrb.lib.directory.api.IDirectoryBean;

public class DirectoryBean implements IDirectoryBean {
    private int id;
    private String name;
    private boolean folder;
    private ArrayList<DirectoryBean> directories;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFolder(boolean folder) {
        this.folder = folder;
    }

    public void setDirectories(ArrayList<DirectoryBean> directories) {
        this.directories = directories;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isFolder() {
        return folder;
    }

    @Override
    public ArrayList<? extends IDirectoryBean> getDirectories() {
        return directories;
    }

    @NotNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
