;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  PATH.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  Jeff Stephenson
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  Motion classes for a path -- i.e. moving to a series of pre-defined
;;;;  points.
;;;;
;;;;  Classes:
;;;;     Path
;;;;     RelPath


(script# PATH)
(include system.sh) (include sci2.sh)
(use Intrface)
(use Motion)


(class Path of MoveTo
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
		centerObj 0
		value 0	;index into path array
	)
	
	(method (init actor toCall inter)
		(= client actor)
		(= caller (if (>= argc 2) toCall else 0))
		(= centerObj (if (== argc 3) inter else 0))
		(= value -1)
		(= x (client x?))
		(= y (client y?))
		(if (self radius:)
			(self moveDone:)
		else
			(self next:)
			(super init: client x y)
		)
	)
	
	(method (moveDone)
		(if (self radius:)
			(super moveDone:)
		else
			(if centerObj (centerObj cue: (/ value 2)))
			(self next:)
			(super init: client x y)
		)
	)
	
	(method (at)
		(Printf {%s needs an 'at:' method.} name)
		(return 0)
	)
	
	(method (next)
		(= x (self at: (++ value)))
		(= y (self at: (++ value)))
	)
	
	(method (radius)
		(return
			(if (== (self at: (+ value 1)) PATHEND)
			else
				(== (self at: (+ value 2)) PATHEND)
			)
		)
	)
)

(class RelPath of Path
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
		centerObj 0
		value 0
	)
	
	(method (next)
		(= x (+ x (self at: (++ value))))
		(= y (+ y (self at: (++ value))))
	)
)
