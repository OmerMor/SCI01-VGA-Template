;;; Sierra Script 1.0 - (do not remove this comment)
(script# TITLE)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Game)
(use System)

(public
	title 0
)

(instance title of Room
	(properties
		picture pSpeedTest
		style DISSOLVE
	)
	
	(method (init)
		(super init: &rest)
		(self setScript: titleScreen)
	)
)


(instance titleScreen of Script
	(properties)
	
	(method (changeState newState)
		(switch (= state newState)
			(0 
				(Display
					"Intro/Opening screen"
					dsCOORD 90 80
					dsCOLOR clWHITE
					dsBACKGROUND clTRANSPARENT
					)
			)
			(1
				(curRoom newRoom: 2)
			)
		)
	)
	(method (handleEvent event)
		(super handleEvent: event)
		(switch (event type?)
			(evKEYBOARD
				(titleScreen changeState: 1)
			)
			(mouseDown
				(titleScreen changeState: 1)
			)
		)
	)
)