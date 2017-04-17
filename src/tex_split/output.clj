(ns tex-split.output
  (:require [clojure.java.io :as io] ))


(defn combineTex
  [content topstr bottomstr]
  (str topstr "\r\n\r\n" content "\r\n\r\n" bottomstr))

(defn mkdirp [path]
  (let [dir (java.io.File. path)]
    (if (.exists dir)
      true
      (.mkdirs dir))))

(defn PartialIndexedFn
  [outputPath topStr bottomStr]
  (fn [item]
    (let [texfile (str outputPath (:key item) (:id item) ".tex")
          texContent (combineTex (:content item) topStr bottomStr)]
      (println texfile)
      (spit texfile texContent :encoding "GBK"))
    ))

(defn Output
  [blockGrp outputPath topStr bottomStr]
  (mkdirp outputPath)
  (let [outputTexGroupFn (PartialIndexedFn outputPath topStr bottomStr)]
    (doall (map outputTexGroupFn blockGrp))))
