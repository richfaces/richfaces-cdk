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
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;

public class Aggregation {
    public boolean removeIncluded = false;
    public boolean insertSemicolon = false;
    public boolean insertNewLine = false;
    public String[] excludes;
    public String[] includes;
    public File output;
    public File sourceDirectory;

    public void run(File outputDirectory) throws Exception {
        if (null == sourceDirectory) {
            sourceDirectory = outputDirectory;
        }

        List<File> files = getIncludedFiles();

        if (files.size() != 0) {
            FileOutputStream out = new FileOutputStream(output);

            try {
                for (File file : files) {
                    FileInputStream in = new FileInputStream(file);

                    try {
                        IOUtil.copy(in, out);

                        if (insertSemicolon) {
                            out.write(';');
                        }

                        if (insertNewLine) {
                            out.write('\n');
                        }

                        if (removeIncluded) {
                            file.delete();
                        }
                    } finally {
                        IOUtil.close(in);
                        in = null;
                    }
                }
            } finally {
                IOUtil.close(out);
                out = null;
            }
        }
    }

    protected List<File> getIncludedFiles() throws Exception {
        ArrayList<File> back = new ArrayList<File>();

        if (includes != null) {
            for (String include : includes) {
                addInto(include, back);
            }
        }

        return back;
    }

    private void addInto(String include, List<File> includedFiles) throws Exception {
        if (include.indexOf('*') > -1) {
            DirectoryScanner scanner = newScanner();

            scanner.setIncludes(new String[] {include});
            scanner.scan();

            String[] rpaths = scanner.getIncludedFiles();

            for (String rpath : rpaths) {
                File file = new File(scanner.getBasedir(), rpath);

                if (!includedFiles.contains(file)) {
                    includedFiles.add(file);
                }
            }
        } else {
            File file = new File(include);

            if (!file.isAbsolute()) {
                file = new File(output.getParentFile(), include);
            }

            if (!includedFiles.contains(file)) {
                includedFiles.add(file);
            }
        }
    }

    private DirectoryScanner newScanner() throws Exception {
        DirectoryScanner scanner = new DirectoryScanner();

        scanner.setBasedir(sourceDirectory);

        if ((excludes != null) && (excludes.length != 0)) {
            scanner.setExcludes(excludes);
        }

        scanner.addDefaultExcludes();

        return scanner;
    }
}
