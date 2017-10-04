/*
 * Copyright 2017 Jason Mar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jasonmar.goconverter

import java.io.{File, FileOutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets
import java.nio.file.Paths

import GoConsts.getConsts
import GoStructs.{getCaseClasses, getJsonFormats}

import scala.io.{Codec, Source}

object Main {

  def readStructsFile(nomadSourceDir: String): Vector[String] = {
    implicit val codec = Codec.UTF8
    val files = Seq("structs.go", "diff.go", "bitmap.go", "funcs.go", "network.go", "node_class.go", "operator.go")
    val paths = files.map(f => Seq(nomadSourceDir, "nomad", "structs", f).mkString(File.separator))
    val lines: Vector[String] = paths.flatMap(f => Source.fromFile(new File(f)).getLines()).toVector
    lines
  }

  def writeConsts(out: String, lines: Vector[String]): Unit = {
    val os = new FileOutputStream(new File(out))
    val w = new OutputStreamWriter(os, StandardCharsets.UTF_8)
    w.write(getConsts(lines.iterator))
    w.flush()
    os.flush()
    w.close()
    os.close()
  }

  def writeCaseClasses(out: String, lines: Vector[String]): Unit = {
    val os = new FileOutputStream(new File(out))
    val w = new OutputStreamWriter(os, StandardCharsets.UTF_8)
    w.write(getCaseClasses(lines.iterator))
    w.flush()
    os.flush()
    w.close()
    os.close()
  }

  def writeJsonFormats(out: String, lines: Vector[String]): Unit = {
    val os = new FileOutputStream(new File(out))
    val w = new OutputStreamWriter(os, StandardCharsets.UTF_8)
    w.write(getJsonFormats(lines.iterator))
    w.flush()
    os.flush()
    w.close()
    os.close()
  }

  val USAGE = "Usage: <golangSrcDir> [outDir]\noutDir defaults to current directory"

  def main(args: Array[String]): Unit = {
    val nomadSourceDir = args.headOption
    val outDir = args.lift(1)

    if (nomadSourceDir.isEmpty) {
      System.err.println(USAGE)
      System.exit(1)
    }

    val outdir = Paths.get("generated")
    val lines: Vector[String] = readStructsFile(nomadSourceDir.get)

    val constsPath = outdir.resolve("Consts.scala")
    val typesPath = outdir.resolve("Types.scala")
    val jsonFormatsPath = outdir.resolve("JsonFormats.scala")
    writeConsts(constsPath.toString, lines)
    writeCaseClasses(typesPath.toString, lines)
    writeJsonFormats(jsonFormatsPath.toString, lines)
  }
}
