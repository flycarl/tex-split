(ns tex-split.split
  (:require [instaparse.core :as insta :refer [defparser]])
  (:require [clojure.java.io :as io])
  )

(defparser text_block (io/resource "myparser.bnf"))

(defn Parseblock
  [midStr]
  (text_block midStr))

(defn beginStr
  [key]
  (str "\\begin{" key "}"))

(defn endStr
  [key]
  (str "\\end{" key "}\r\n"))

(defn blockStr
  [[_  key] content]

  {:key key :content (str
                      (beginStr key)
                      content
                      (endStr key))}
  )

(defn countExample
  [idx d1]
  (->> (take (+ 1  idx) d1)
       (filter #(= "example" (:key %)))
       (count )
       )
  )

(defn mapExampleId
  [d1]
  (map-indexed
   (fn [idx item]
     {:id (countExample idx d1)
      :key (:key item)
      :content (:content item)} )
   d1))

(defn ParseblockGrp
  [midStr]
  (->>
   (Parseblock midStr)
   (insta/transform {:content str} )
   (rest )
   (insta/transform {:block blockStr} )
   (filter #(> (count %) 1) )
   (map #(second %) )
   (mapExampleId )
   (partition-by #(:id (first %)) )
   (flatten )
   ))

;test
;(def midStr (slurp (io/resource "test.tex")))
;(ParseblockGrp midStr)

