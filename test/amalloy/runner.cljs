(ns amalloy.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [amalloy.ring-buffer-test]))

(doo-tests 'amalloy.ring-buffer-test)
