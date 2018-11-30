;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  MOVEFWD.SC
;;;;  (c) Sierra On-Line, Inc, 1990
;;;;
;;;;  Author: J.Mark Hood
;;;;
;;;;  Moves an Actor forward depending on his heading.
;;;; Cues caller when completed as usual for movers.
;;;;  Size:
;;;;     194 bytes for initial module.
;;;;
;;;;  Classes:
;;;;     MoveFwd
;;;;
;;;;  Usage:
;;;;     anActor setMotion: MoveFwd 20 whoToCue
;;;;

(script# MOVEFWD)
(include system.sh) (include sci2.sh)
(use Motion)


(class MoveFwd of MoveTo
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
	)
	
	(method (init actor dist toCall)
		(super
			init:
				actor
				(+ (actor x?) (SinMult (actor heading?) dist))
				(- (actor y?) (CosMult (actor heading?) dist))
				(if (>= argc 3) toCall else 0)
		)
	)
)
