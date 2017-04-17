(ns tex-split.core
  (:require [clojure.tools.cli :refer [parse-opts]]
            [tex-split.split :as texSplit]
            [tex-split.output :as texOutput])
  (:gen-class)
  (:import java.io.File))

(def cli-options
  [
   ["-b" "--basePath BASE_PATH" "Base path"
    :id :basePath]
   ["-i" "--input INPUT_PATH" "Input tex relative path"
    :default "InputTex"
    :id :input]
   ["-o" "--output OUTPUT_PATH" "Output tex relative path"
    :default "OutputTex"
    :id :output]
   ["-h" "--help"]])

(defn OutPutPath
  [baseStr output]
  (let [sep (java.io.File/separator)]
    (str baseStr sep output sep)))
(defn InputPath 
  [baseStr input]
  (let [sep (java.io.File/separator)]
    (str baseStr sep input sep)))
(defn MidStr
  [inputPath]
  (slurp (str inputPath "Mid.tex") :encoding "GBK"))
(defn TopStr
  [inputPath]
  (slurp (str inputPath "Top.tex") :encoding "GBK"))
(defn BottomStr
  [inputPath]
  (slurp (str inputPath "Bottom.tex") :encoding "GBK"))

(defn -main
  [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        basePath (:basePath options)
        input (:input options)
        output (:output options)
        inputPath (InputPath basePath input)
        outputPath (OutPutPath basePath output)
        midStr (MidStr inputPath)
        topStr (TopStr inputPath)
        bottomStr (BottomStr inputPath)
        blockGrp (texSplit/ParseblockGrp midStr)
        ]
    (texOutput/Output blockGrp outputPath topStr bottomStr)))

;(-main "-b /Users/flycarl/Downloads")

