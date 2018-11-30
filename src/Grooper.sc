;;; Sierra Script 1.0 - (do not remove this comment)
;;;;
;;;;  GROOPER.SC
;;;;
;;;;  (c) Sierra On-Line, Inc, 1992
;;;;
;;;;  Author:  J. Mark Hood
;;;;  Updated: Brian K. Hughes
;;;;
;;;;  This class is the Gradual Looper, which turns an actor from his current
;;;;  loop through all intermediate loops to his destination loop.
;;;;
;;;;  Classes:
;;;;     Grooper
;;;;     Grycler


(script# GROOPER)
(include system.sh) (include sci2.sh)
(use Main)
(use Sight)
(use Motion)
(use System)

;;; GradualLooper Class
;;; Author J.Mark Hood 4/20/89
;;; Loops actors smoothly through normal loops
;;; on a direction change.
;;; Works for  4 loop or 8 loop actors
;;; Note: looper checks number of loops in clients view,
;;; so don't put  8 loops in the view and expect client to
;;; only use the first four.  
;;; Also note that Grooper uses AngleDiff from sight.sc.
;;; If you do not use sight.sc but wish to use the Grooper
;;; Compile Anglediff yourself or make use (extern AngleDiff SIGHT publicNum).
;;; 640 bytes alone or 590 + sight.
;;; Usage : (actor setLoop:GradualLooper)


;;; Changes to accomodate single stopwalk views (views in which the last loop
;;;   contains stopped cels, in loop order):
;;;
;;;   1) when calculating lastDir (Grooper doit:), use ego's cel number instead
;;;      of his loop number, since his loop will always be the last one
;;;
;;;   2) if loop is already correct (Grycler init:), we set ego's loop from
;;;      the last one (stopped cels) to the appropriate walking loop before
;;;      invoking cycleDone.


(local
	[trans1 8] = [2 6 4 0 3 5 1 7]	; loop #s for E W S N SE SW NE NW
	[trans2 8] = [loopN  loopNE loopE loopSE loopS loopSW loopW loopNW]	; Saves on heap consumption over using a switch

)
(class GradualLooper of Code
	(properties
		name "Grooper"
		client 0
		oldCycler 0
		oldMover 0
		caller 0
	)
	
	(method (doit c h whoCares dontGroop &tmp lastDir numLoops)
		(if (not client) (= client c)
			)
			
			; Bail out if the client has his fixedLoop bit set
		(if (& (client signal?) fixedLoop)
			(if caller
				(caller cue:)
			)
			(= caller 0)
			(return)
		)
		(if (>= argc 3) (= caller whoCares))
		(= numLoops	; for now only allow 4 or 8 loop actors
			(if (< (NumLoops client) 8) 4 else 8)
			)
		(if
			(or
				(not (cast contains: client)) ; just go to loop if not in cast
				(and (>= argc 4) dontGroop) ; modification to help avoider out
			)
			(client	; just go to proper loop without
				loop:
					[trans2 (*
						(if (== numLoops 4) 2 else 1)
						(/
							(umod (+ (client heading?) (/ 180 numLoops)) 360)
							(/ 360 numLoops)
						)
					)]
			) ; gradual turn (i.e. an 8 loop DirLoop)		
			(if caller (caller cue:))
			(= caller 0)
			(return)
		)
		
		(= lastDir [trans1 (client loop?)])
		(if oldMover (oldMover dispose:) (= oldMover 0))
		(if
			(and
				(IsObject oldCycler)
				(or
					(oldCycler isMemberOf: GradualCycler)
					(not ((client cycler?) isMemberOf: GradualCycler))
				)
			)
			(oldCycler dispose:)
			(= oldCycler 0)
		)
		(if (not oldCycler) (= oldCycler (client cycler?)))
		(if
			(and
				(client cycler?)
				((client cycler?) isMemberOf: GradualCycler)
			)
			((client cycler?) dispose:)
		)
		(= oldMover (client mover?))
		(client
			cycler: 0
			mover: 0
			setMotion: 0
			setCycle: GradualCycler self lastDir
		)
	)	;doit
	
	(method (dispose)
		(if (IsObject oldCycler)
			(oldCycler dispose:)
			(= oldCycler 0)
		)
		(if (IsObject oldMover)
			(oldMover dispose:)
			(= oldMover 0)
		)
		(if client (client looper: 0))
		(super dispose:)
	)	;dispose
	
	(method (cue &tmp whoCares)
		(if (not (IsObject (client mover?)))
			(client mover: oldMover)	; setMotion would be recursive
		)
		(if (IsObject oldCycler) (client cycler: oldCycler))
		(= whoCares caller)
		(= caller (= oldMover (= oldCycler 0)))
		(if whoCares (whoCares cue: &rest))
	)
)	;cue

(class GradualCycler of Cycle
	(properties
		name "Grycler"
		client 0
		caller 0
		cycleDir 1
		cycleCnt 0
		completed 0
		loopIndex 0
		numOfLoops 0
	)
	
	(method (init act whoCares oldDir)
		(super init: act)
		(= caller whoCares)
		(= numOfLoops	; for now only allow 4 or 8 loop actors
			(if (< (NumLoops client) 8)
				4
				else 8
				)
			)
		(= cycleDir
			(-
				(sign
					(AngleDiff (* oldDir 45) (act heading?))
				)
			)
		)
		(= loopIndex oldDir)
		(if (self loopIsCorrect:) (self cycleDone:)
		)
	) ;init
	
	(method (doit)
		(client loop: (self nextCel:)) ; sorry but this class is a hybrid
		(if (self loopIsCorrect:) (self cycleDone:))
	)	;doit
	
	(method (nextCel)
		;; Return client's next logical cel.
      ;Increment the number of animation cycles since the client last cycled.
		(++ cycleCnt)
		(return
			(if
				(or
					(<= cycleCnt (client cycleSpeed?))
					(self loopIsCorrect:)
					;Not yet time to change the client's cel
				)
				(client loop?) ;return value

			else
				(= cycleCnt 0)
				(= loopIndex
					(+ loopIndex (* cycleDir (/ 8 numOfLoops)))
				)
				(= loopIndex (umod loopIndex 8))
				[trans2 loopIndex]	;return value
			)
		)
	)
	
	(method (cycleDone)
		(= completed TRUE)
      (= doMotionCue TRUE)
	) ;cycleDone
	
	(method (loopIsCorrect)
		(return
			(<
				(Abs (AngleDiff (* loopIndex 45) (client heading?)))
				(+ (/ 180 numOfLoops) 1)
			)
		)
	)
)
