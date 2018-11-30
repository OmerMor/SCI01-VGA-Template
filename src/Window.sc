;;; Sierra Script 1.0 - (do not remove this comment)
;; The window class defines visible BORDERED rectangular areas of the screen.
;; A window, ID'ed by systemWindow, is used by Dialog to specify various
;; aspects of its appearence. 


(script# WINDOW)
(include system.sh) (include sci2.sh)
(use Save)


(class Window of SysWindow
	(properties
		top 0
		left 0
		bottom 0
		right 0
		color 0		; foreground color
		back 15		; background color
		priority -1	; priority
		window 0	; handle/pointer to system window
		type 0		; generally corresponds to system window types
		title 0		; text appearing in title bar if present
		;; this rectangle is the working area for X/Y centering
		;; these coordinates can define a subsection of the picture
		;; in which a window will be centered
		brTop 0
		brLeft 0
		brBottom 190
		brRight 320
		underBits 0
	)
	
	(method (doit)
	)
	
	(method (dispose)
		(self restore:)
		(if window (DisposeWindow window) (= window 0)
			)
		(super dispose:)
	)

   ;; Open corresponding system window structure
   ;; Custom window type 0x81 indicates that system
   ;; will NOT draw the window, only get a port and link into list	
	(method (open)
		(= window
			(NewWindow
				;; port dimensions
				top
				left
				bottom
				right
				title
				type
				priority
				color
				back
			)
		)
	)
	
	(method (handleEvent)
		(return FALSE)
	)
	
   (method (setMapSet &tmp mapSet)
      (= mapSet 0)
      (if (!= -1 color)
         (|= mapSet VMAP)
      )
      (if (!= -1 priority)
         (|= mapSet PMAP)
      )
      (return mapSet)
   )
	
	(method (move h v)
      (+= left h)
      (+= right v)
      (+= right h)
      (+= bottom v)

	)
	
	(method (moveTo h v)
		(self move: (- h left) (- v top))
	)
	
	(method (draw v p)
		(if (>= argc 1) (= color v))
		(if (>= argc 2) (= priority p))
		(Graph GFillRect top left bottom right (self setMapSet:) color priority)

	)
	
	(method (save)
		(= underBits
			(Graph GSaveBits top left bottom right (self setMapSet:))
		)
	)
	
	(method (restore)
		(if underBits
			(Graph GRestoreBits underBits))
	)
	
   (method (inset h v)
      (+= top v)
      (+= left h)
      (-= bottom v)
      (-= right h)
   )
	
	(method (show)
		(Graph GShowBits top left bottom right (self setMapSet:))
	)
	
	(method (angleStep)
		(self draw: back -1)
	)
	
	(method (center)
		;; Center the window in the working rectangle.
		(self
			moveTo:
				(/ (- (- brRight left) (- right left)) 2)
				(/ (- (- brBottom top) (- bottom top)) 2)
		)
	)
)
