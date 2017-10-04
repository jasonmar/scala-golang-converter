## Description

Generates Scala code from a Go source directory.

## Motivation

If you are working with a Go application and would like to copy the object model to produce case classes for building request or response JSON payloads, this could potentially save some time.

## Features

* Generates Scala case classes from Go structs, including resolution of common fields
* Generates Scala vals from Go consts, handles all native types

## Usage

```
sbt "run-main com.jasonmar.goconverter.Main <golangSrcDir> [outDir]
```
