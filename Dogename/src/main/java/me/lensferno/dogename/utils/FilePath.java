package me.lensferno.dogename.utils;

import java.io.File;

public final class FilePath {
    public static String toSpecificPathForm(String uniformFilepath) {
        return uniformFilepath.replace("/", File.separator);
    }
}
