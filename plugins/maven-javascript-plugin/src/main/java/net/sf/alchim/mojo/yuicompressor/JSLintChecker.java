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
import java.io.FileOutputStream;
import java.io.InputStream;

import org.codehaus.plexus.util.IOUtil;

import org.mozilla.javascript.ErrorReporter;

//TODO: use MojoErrorReporter
class JSLintChecker {
    private String jslintPath_;

    public JSLintChecker() throws Exception {
        FileOutputStream out = null;
        InputStream in = null;

        try {
            File jslint = File.createTempFile("jslint", ".js");

            in = getClass().getResourceAsStream("/jslint.js");
            out = new FileOutputStream(jslint);
            IOUtil.copy(in, out);
            jslintPath_ = jslint.getAbsolutePath();
        } finally {
            IOUtil.close(in);
            IOUtil.close(out);
        }
    }

    public void check(File jsFile, ErrorReporter reporter) {
        String[] args = new String[2];

        args[0] = jslintPath_;
        args[1] = jsFile.getAbsolutePath();
        BasicRhinoShell.exec(args, reporter);

        // if (Main.exec(args) != 0) {
        // reporter.warning("warnings during checking of :" + jsFile.getAbsolutePath(), null, -1, null, -1);
        // }
    }
}
