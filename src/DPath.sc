;;; Sierra Script 1.0 - (do not remove this comment)
;;;;  DPATH.SC    (c) Sierra On-Line, Inc, 1989
;;;;
;;;;  Author: Pablo Ghenis
;;;;
;;;;  D(ynamic)PATH uses a dynamically created list to keep path
;;;;  points.
;;;;  
;;;;  Usage is like other motion classes:
;;;;  
;;;;     (anActor setMotion DPath x1 y1 x2 y2 ...  anOptionalCaller)
;;;;  
;;;;  (also see PATH.SC for static path class)
;;;;

(script# DPATH)
(include system.sh) (include sci2.sh)
(use Motion)
(use System)


(class DPath of Motion
	(properties
		client 0
		caller 0
		x 0
		y 0
		dx 0
		dy 0
		b-moveCnt 0
		b-i1 0
		b-i2 0
		b-di 0
		b-xAxis 0
		b-incr 0
		completed 0
		xLast 0
		yLast 0
		points NULL  ;dynamic list to hold path points
		value 0     ;index into path array

	)
	
	(method (init theClient thePoints &tmp i)
		(= points (if points else (List new:)))	;create only once
		
		(if argc
			(= client theClient)
			(= i 0)
			(while (<= i (- argc 3))
				(points add: [thePoints i] [thePoints (++ i)])
				(++ i)
			)
			;final odd argument, if present, is caller
			(if (<= i (- argc 2)) (= caller [thePoints i]))
		)
		(or 
         (points contains: PATHEND)
         (points add: PATHEND)       ;terminate only once
      )

		(self setTarget:)
		(super init:)
		
		;; eliminate pause at each node of path.
		(if (not argc)
			(self doit:)
		)
	)
	
	(method (dispose)
		(if (IsObject points) (points dispose:))
		(super dispose:)
	)
	
	(method (moveDone)
		(if (== (points at: value) PATHEND)
			(super moveDone:)
		else
			(self init:)
		)
	)
	
	(method (setTarget)
		(if (!= (points at: value) PATHEND)
			(= x (points at: value))
			(= y (points at: (++ value)))
			(++ value)
		)
	)
);DPath
