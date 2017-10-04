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

object GoTypes {
  def replaceTypes(goType: String, recurseDepth: Int = 0): String = {
    goType match {
      case x if x == "string" => "String"
      case x if x == "bool" => "Boolean"
      case x if x == "int" => "Int"
      case x if x == "int32" => "Int"
      case x if x == "int64" => "Long"
      case x if x == "[]string" => "Array[String]"
      case x if x == "[]byte" => "Array[Byte]"
      case x if x == "uint32" => "Long"
      case x if x == "uint64" => "Long"
      case x if x == "float32" => "Float"
      case x if x == "float64" => "Double"
      case x if x == "net.IP" => "String"
      case x if x == "time.Duration" => "Long"
      case x if x == "time.Time" => "Long"
      case x if x == "time.Location" => "String"
      case x if x == "raft.ServerID" => "String"
      case x if x == "raft.ServerAddress" => "String"
      case x if x == "interface{}" => "Any"
      case x if x == "int8" => "Int"
      case x if x == "int16" => "Int"
      case x if x == "uint8" => "Int"
      case x if x == "uint16" => "Int"
      case x if x == "float8" => "Float"
      case x if x == "float16" => "Float"
      case x if recurseDepth < 2 && x.charAt(0) == '*' && x.length > 1 =>
        replaceTypes(x.substring(1, x.length), recurseDepth + 1)
      case x if recurseDepth < 1 && x.take(4) == "map[" =>
        val split = x.substring(4, x.length).split("]")
        s"Map[${replaceTypes(split.head, recurseDepth + 1)},${replaceTypes(split.last, recurseDepth + 1)}]"
      case x if recurseDepth < 1 && x.take(2) == "[]" =>
        "Array[" + replaceTypes(x.substring(2,x.length).replace("*",""), recurseDepth + 1) + "]"
      case _ =>
        goType
    }
  }
}
