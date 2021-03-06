# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## 1.3.1 - 2019-12-01

* Changed: Clojure and ClojureScript are now marked as `:scope "provided"` in project.clj

## 1.3.0 - 2018-12-15

### Added

* Added support for `nth` lookups (also with negative indices)

## 1.2.2 - 2018-12-07

### Fixed

* Fixed ambiguous Collection.toArray definition under Java 11 [#11](https://github.com/clj-commons/ring-buffer/pull/11)

## 1.2.1 - 2016-06-24

### Added

* Added support for `rseq`/the `Reversible` interface.
