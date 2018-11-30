;;; Sierra Script 1.0 - (do not remove this comment)
(script# FOLLOW)
(include system.sh) (include sci2.sh)
(use Motion)


(class Follow of Motion
	;;; This class moves its client in such a way as to try and stay within
	;;; a certain distance to another object.

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
		who 0             ;who to follow
		distance 20       ;try to stay at least this close to 'who'
	)
	
	(method (init theObj whom dist)
		(if (>= argc 1)
			(= client theObj)
			(if (>= argc 2)
				(= who whom)
				(if (>= argc 3) (= distance dist))
			)
		)
		;If the client is too far from the object being followed, start
		;moving toward it.
		(if (> (client distanceTo: who) distance)
			(super init: client (who x?) (who y?))
		)
	)
	
	(method (doit &tmp angle)
		(if (> (client distanceTo: who) distance)
			(if (== b-moveCnt 0)
				(super init: client (who x?) (who y?))
			)
			;If too far from the object being followed, move toward it.
			(super doit:)
		else
		;The client is just standing around near its destination.  Pick
		;the loop which faces in the destination's direction.
			(= xLast (client x?))
			(= yLast (client y?))
			(= angle (GetAngle xLast yLast (who x?) (who y?)))
			(client heading: angle)
			(if (client looper?)
				((client looper?) doit: client angle)
			else
				(DirLoop client angle)
			)
		)
	)
	
	(method (moveDone)
	)
	
	(method (setTarget)
		(cond 
			(argc (super setTarget: &rest))
			((not (self onTarget:)) (super setTarget: (who x?) (who y?)))
		)
	)
	
	(method (onTarget)
		(return (<= (client distanceTo: who) distance))
	)
)
