;; NO HANDOUTS TODAY!!
;; we don't need a handout
;; Handouts kill trees
;; You should feel bad for wanting a description

(ns adventure.core
  (:require [clojure.core.match :refer [match]]
            [clojure.string :as str])
  (:gen-class))

(defn get-random-string []
  (apply str (for [pos (range 200)]
    (char (+ 33 (int (* (Math/random) 93)))))))

  (def the-map
    {:foyer {:desc "The walls are freshly painted but do not have any pictures. Almost as though the paint is designed to cover up some sort of smell...your gut warns you to get as far away from this place as possible. There's a grue pen to the south and an exit from the house to the west."
             :title "in the foyer"
             :dir {:south :grue-pen
                   :west  :white-house}
             :contents #{:raw-egg}}
     :grue-pen {:desc "It is very dark.  You might've been eaten by a grue, but it seems the grue is out for groceries today. How lucky for you."
                :title "in the grue pen"
                :dir {:north :foyer}
                :contents #{:banjo-string}}
     :white-house {:desc "You have climbed out through the east window instead of going through the door and feel rather foolish. Looking south, you see a port. To the north are some promisingly complex hedges."
                :title "outside the white house"
                :dir {:east :foyer
                      :south :port-floozle
                      :north :maze-entrance}
                :contents #{}}
     :port-floozle {:desc "You stand near the harbor of Port Floozle. There are big boats in the water, but you can't get on any of them. What a shame. You do, however, see a long winding road to the south, and a seemingly pleasant beach to the west."
                :title "in Port Floozle"
                :dir {:north :white-house
                      :south :road-to-the-south
                      :west :quilbozza-beach}
                :contents #{}}
     :quilbozza-beach {:desc "You stand in the pinkish-white sand of Quilbozza Beach and wriggle your toes. How pleasant. You see the port again to your east, and a long winding road to the south."
                :title "on Quilbozza Beach"
                :dir {:east :port-floozle
                      :south :road-to-the-south}
                :contents #{:pocket-sand}}
     :maze-entrance {:desc "There is a path north between the hedges, but it's blocked by a tree branch. You can hear your spleen telling you this path is crucial to getting away from the white house. It also tells you that you're about to enter a maze and should make sure nothing was missed before proceeding."
                :title "at the maze entrance"
                :dir {:south :white-house
                      :north :maze-first-step}
                :contents #{}}
     :road-to-the-south {:desc "You stand on the road to the south. Everyone has warned you it is impassible. Absolutely impossible to pass. Impassible. But now that you have begun this path, you may not turn back. This is your life now, ya goof..."
                :title "on the Road To The South. Don't expect it to end. Ever"
                :dir {:south :road-to-the-south
                      :north :road-to-the-south
                      :east :road-to-the-south
                      :west :road-to-the-south}
                :contents #{:poison}}
     :maze-first-step {:desc "You have taken the first proper step into the maze. Good for you! Unfortunately, now you cannot turn back. In fact, you've been poisoned because your body's immune system is unaccustomed to the smell of pineapple marmalade. Better get out of this maze quick, buddy. There are a bunch of palm trees to your north and more convenient hedges to the east."
                :title "in the maze"
                :dir {:north :miznia
                      :east :maze-second-step}
                :contents #{:pineapple-marmalade}
                :enter-function (fn [player]
                          (update player :poisoned (fn [i] true)))}; check if player was already poisoned?
     :miznia {:desc "You have entered Miznia. Unfortunately, this bundle of palm trees are all that remains due to excessive deforestation and an eleventeen decade long drought. Your left inner thigh tells you this isn't the right direction."
                :title "in Miznia"
                :dir {:south :maze-first-step}
                :contents #{:crocodile-tear}}
     :maze-second-step {:desc "You enter deeper into the maze. Your surroundings grow darker and the foliage becomes much more menacing. How spoopy. The path north smells fishy, and the path further east doesn't exist."
                :title "in the maze"
                :dir {:west :maze-first-step
                      :north :maze-fish-lock
                      :east :treasure-room}
                :contents #{}}
     :treasure-room {:desc "Why tho. Y u make me do dis. I tol' u der's no ruum h3r3."
                :title "in the treasure room"
                :dir {:west :maze-second-step}
                :contents #{:lots-o-booootybootybooty}}
     :maze-fish-lock {:desc "There is a door to your east with a suspiciously fish shaped lock. Your right cornea thinks you might want to drop something similarly shaped and smelling to continue east. Also, you hear a rumbling noise to the north."
                :title "by a fishy door"
                :dir {:south :maze-second-step
                      :north :aragain-falls}
                :contents #{}
                :drop-item-function (fn [player item]
                  (if (= item :red-herring)
                      (do
                        (println "As you are distracted, tryng to fit the red herring into the lock, another fish suddenly flups into the room and unlocks the door for you. You may now continue East.")
                        (def the-map
                          (assoc-in the-map [:maze-fish-lock :dir :east] :maze-final-puzzle))
                        player)
                      player))}
     :maze-final-puzzle {:desc "The fishy door slams shut behind you, but does not lock. You now approach what seems like a complex, weight-based puzzle. The giant stone tablet by the eastern doors inform you that, in order to escape the maze, you need to drop 1 item similarly weighted to the quantity of Jupiter divided by Venus multiplied by 1000 paperclips, subtracted from 29 wheelbarrows. Good luck."
                :title "standing by the final puzzle"
                :dir {
                      :west  :maze-fish-lock}
                :contents #{:scale}
                :drop-item-function (fn [player item]
                    (if (= item :crocodile-tear)
                        (do (println "The Crocodile Tear dissolves upon contact with the non-detritus ground. The doors open, revealing a new eastern path leading out of the maze.")
                        (def the-map
                          (assoc-in the-map [:maze-final-puzzle :dir :east] :maze-exit))
                        player)
                        player))}
     :maze-exit {:desc "Congratulations! You have escaped the maze and the white house no longer poses any danger to you. You have successfully survived another Thanksgiving family reunion. PleA=a=seee contniniue north for credits and srturff."
                :title "outside the maze"
                :dir {:north :post-maze-credits}
                :contents #{:antidote}}
     :aragain-falls {:desc "You see a beautiful waterfall simmering with aquatic life. You notice a grue holding a fishing pole by the edge."
                :title "by Aragain Falls"
                :dir {:south :maze-fish-lock}
                :contents #{:red-herring}}
     :post-maze-credits {:desc "I wonder why the game developers would leave this pointless room that totally doesn't continue *ehem*northwards*ehem* here."
                :title "in an empty hallway"
                :dir {:north :final-grue-friend
                      :south :maze-exit}
                :contents #{:nothing}
     }
     :final-grue-friend {:desc "You are with your soulmate, perfectly content, sitting around a fading campfire."
     :title "around a campfire"
     :dir {}
     :contents #{}
     :enter-function (fn [player]
       (do (println "You look around and notice that the sun has set, and the winter frost begins to kick in. In the distance, you see a lone campfire glowing against the oppressive darkness.")
          (Thread/sleep 5000)
          (println "As you approach the campfire, you notice a single hooded figure strumming a slow tune on his banjo, and his deep, soothing humming gently floats into your ears.")
          (Thread/sleep 7000)
          (println "An incredible sense of calm washes over you as finally join the humming figure at his campfire. Its warmth seeping into your bones, making you momentarily forget the biting winter frost raging around you.")
          (Thread/sleep 8000)
          (println "You glance over at your savior, and you see glistening teeth peek out of his hood. He notices your presence, but tells you not to worry - the campfire tempers his appetite. He tells you that, instead of craving adventurer's flesh, all he craves is a friend to confide in.")
          (Thread/sleep 10000)
          (println "He tells his life story - a mournful tale of romance and greed. Of dastardly betrayal and love lost. Of ingenius escapes and the cleverest of revenges. Of empty satisfaction and a disconnect from reality.")
          (Thread/sleep 8000)
          (println "He tells of how he fought against his natural instincts to devour and consume all living creatures that come before him, and how he became what you see before you today - a lonely traveller, drifting from place to place, town to town, never quite fitting in, always facing rejection.")
          (Thread/sleep 10000)
          (println "He tells you about how he continues to hope against hope, to fight against fate, to deny his destiny - to behave in a manner more fitting of his true nature - an equine. A magical, single horned equine. Better known as an unicorn.")
          (Thread/sleep 9000)
          (println "He falls silent as his tale reaches its conclusion, and the two of you sit in silence, basking in the wramth of the grue's campfire. But it's not an uncomfortable silence. Oh no, this is the silence which exists between two individuals who have come to understand each other's souls so well, so prestinely, so perfectly, words are no longer necessary to communicate thoughts or emotions. In your heart of hearts, you know that the two of you will continue on many adventures...together. <3")
          (Thread/sleep 16000)
          (println "The west wind begins to die out, and with it, the campfire. Darkness slowly engulfs your small sanctuary. You glance one final time at your newfound companion, and, for the first time, he meets your eyes, knowing his appearance can no longer terrify you.")
          (Thread/sleep 5000)
          (println "He opens his mouth, takes in a new breath, and tells you that he")
          (Thread/sleep 2000)
          (doseq [line (range 50)]
            (print (get-random-string))
            (Thread/sleep 100))
          (println)
          (println "ERROR: SEGFAULT -- CORE DUMP")
          (Thread/sleep 5000)
          (println)
          (println "Game created by Piotr Galusza and Jonathan Hoelzel for CS 296.")
          (Thread/sleep 5000)
          (println "Thanks for playing!")
          (Thread/sleep 3000)
          (System/exit 0)))}
     })

(def adventurer
  {:location :foyer
   :inventory #{}
   :health 20
   :poisoned false
   :seen #{}})

(def direction-map
  {:north :north
    :n :north
    :south :south
    :s :south
    :east :east
    :e :east
    :west :west
    :w :west})

(def weight-map
  {:raw-egg "50 grams"
   :poison "100 ounces"
   :lots-o-booootybootybooty "7 metric tons. Good job carrying this around."
   :antidote "100 mililiters.  This scale is not good with units."
   :scale "You can't weigh the scale! That's not allowed!"
   :nothing "0.073 nanometers.  How is this scale even measuring distance?!"
   :red-herring "Why are you weighing this item? It's clearly not important!"
   :crocodile-tear "Approximately the quantity of Jupiter divided by Venus multiplied by 1000 paperclips, subtracted from 29 wheelbarrows. How convenient!"
   :banjo-string "About 5 pounds. It was a deluxe edition banjo."
   :pocket-sand "1307 grains. Surprise!"
   :pineapple-marmalade "Why would you carry something whose mere presence was capable of poisoning you? o.O"})

(defn print-items [location]
  (do
    (println "You see the following items:")
    (doseq [item (:contents location)]
      (print (str (name item) "  ")))
    (println)))

(defn print-inventory [player]
  (do
    (println "Inventory:")
    (doseq [item (:inventory player)]
      (print (name item)))
    (println)
    player))

(defn print-health [player]
  (do
    (print "Health: ")
    (println (:health player))
    player))

(defn status [player]
  (let [location (player :location)]
    (print (str "You are " (-> the-map location :title) ". "))
    (when-not ((player :seen) location)
      (do (println (-> the-map location :desc)))
          (print-items (location the-map)))
    (update-in player [:seen] #(conj % location))))

(defn to-keywords [commands]
  (mapv keyword (str/split commands #"[.,?! ]+")))

(defn do-poisn-dmg [player]
  (if (:poisoned player)
    (do
      (println "You take 1 hp of damage from poison.")
      (if (< (:health player) 2)
        (do
          (println "You have died from poison. That sux.")
          (System/exit 0)))
      (update-in player [:health] #(- % 1)))
    player))

(defn go [dir player]
  (if (nil? dir)
      (do (println "That is not a valid direction.")
        player)
    (let [location (player :location)
          dest (->> the-map location :dir dir)]
      (if (nil? dest)
        (do (println "You can't go that way.")
            player)
        (let [updated-player (do-poisn-dmg (assoc-in player [:location] dest))
              enter-funct (->> the-map dest :enter-function)]
            (if (nil? enter-funct) updated-player
                  (enter-funct updated-player)))))))

(defn get-obj [player object]
  (if (contains? (:contents ((:location player) the-map)) object)
    (do
        (println (str "You picked up the " (name object)))
        (def the-map
          (update-in the-map [(:location player) :contents]
            #(disj % object)))
        (update-in player [:inventory]
          #(conj % object)))
    (do (println "That item is not here.")
        player)))

(defn drop-obj [player object]
  (if (contains? (:inventory player) object)
    (let [updated-player (do
                          (println (str "You put down the " (name object)))
                          (def the-map
                            (update-in the-map [(:location player) :contents]
                              #(conj % object)))
                          (update-in player [:inventory]
                            #(disj % object)))
        drop-funct (:drop-item-function ((:location updated-player) the-map))]
        (if (nil? drop-funct)
              updated-player
              (drop-funct updated-player object)))
    (do (println "You don't have that object.")
        player)))

(defn drink-item [player item]
  (if (contains? (:inventory player) item)
    (cond
      (= item :antidote)
        (do
          (println "Your poisoning has been cured?")
          (update
             (update player :poisoned (fn [i] false))
             :inventory #(disj % :antidote)))
      (= item :poison)
        (do (println "Your lack of poisoning has been cured :)")
        (update
           (update player :poisoned (fn [i] true))
           :inventory #(disj % :poison)))
      (= item :pineapple-marmalade)
        (do (println "You take a miniscule sip of the marmalade and instantly regret it. Why would you drink this!  You can feel your organs failing at an accelerated pace.  -5 health for you!")
        (let [updated-player (update-in player [:health] #(- % 5))]
          (if (< (:health updated-player) 1)
            (do (println "You ded.")
              (System/exit 0))
            updated-player)))
      :else (do
        (println "You can't drink that item.")
        player))
    (do
      (println "You don't have that item.")
      player)))

(defn weigh-item [player item]
  (let [inv (:inventory player)]
      (if (contains? inv :scale)
          (if (contains? inv item)
              (do (println (str "Weight: " (item weight-map)))
                  player)
              (do (println "You do not have that item.")
                  player))
          (do (println "You do not have a way to weigh things!")
              player))))

(defn respond [player commands]
  (match commands
        [:look] (update-in player [:seen] #(disj % (:location player)))

        [(:or :n :north :s :south :e :east :w :west)]
            (go ((first commands) direction-map) player)

        [(:or :go :move :travel :relocate) _]
            (go ((second commands) direction-map) player)

        [(:or :get :take :grab :lift :acquire :obtain) _]
            (get-obj player (second commands))

        [(:or :drop :release :surrender :abandon :place :put :dispense :un-hold) _]
            (drop-obj player (second commands))

        [(:or :health :owie :h :hp :life)]
            (print-health player)

        [(:or :inv :inventory :bag :stuff :items)]
            (print-inventory player)

        [(:or :drink :consume :slurp :devour :chug :guzzle) _]
            (drink-item player (second commands))

        [(:or :weigh :measure :weight) _]
            (weigh-item player (second commands))

        [:help] (do (println "@TODO: add help command") player)

        [(:or :quit :q :exit :escape :get-me-out-of-here)]
            (do (println "No, I'm not done with you yet,") player)

         _  (do (println "I don't understand you.")
            player)
         ))

(defn -main
  "Plays the game."
  [& args]
  (loop [local-map the-map
         local-player adventurer]
    (let [pl (status local-player)
          _  (println "What do you want to do?")
          command (read-line)]
      (recur local-map (respond pl (to-keywords command))))))
