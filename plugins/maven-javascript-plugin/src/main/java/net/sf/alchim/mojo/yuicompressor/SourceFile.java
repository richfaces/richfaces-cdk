/**
 * License Agreement.
 *
 * YUI Compressor Maven Mojo
 *
 * Copyright (C) 2007 Alchim31 Team
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 */



package net.sf.alchim.mojo.yuicompressor;

import java.io.File;

public class SourceFile {
    private boolean destAsSource_;
    private File destRoot_;
    private String extension_;
    private String rpath_;
    private File srcRoot_;

    public SourceFile(File srcRoot, File destRoot, String name, boolean destAsSource) throws Exception {
        srcRoot_ = srcRoot;
        destRoot_ = destRoot;
        destAsSource_ = destAsSource;
        rpath_ = name;

        int sep = rpath_.lastIndexOf('.');

        if (sep > 0) {
            extension_ = rpath_.substring(sep);
            rpath_ = rpath_.substring(0, sep);
        } else {
            extension_ = "";
        }
    }

    public File toFile() {
        String frpath = rpath_ + extension_;

        if (destAsSource_) {
            File defaultDest = new File(destRoot_, frpath);

            if (defaultDest.exists() && defaultDest.canRead()) {
                return defaultDest;
            }
        }

        return new File(srcRoot_, frpath);
    }

    public File toDestFile(String suffix) {
        return new File(destRoot_, rpath_ + suffix + extension_);
    }

    public String getExtension() {
        return extension_;
    }
}
