;;; Sierra Script 1.0 - (do not remove this comment)
(script# TESTROOM)
(include system.sh) (include sci2.sh) (include game.sh)
(use Main)
(use Feature)
(use Game)
(use Main)
(use System)

(public
	rm002 0
)

(instance rm002 of Room
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(StatusLine enable:)
		(super init:)
		(self setScript: RoomScript)
		(switch prevRoomNum
			(else 
				(ego posn: 146 153 loop: 1)
			)
		)
		(SetUpEgo)
		(UnlockEgo)
		(ego init:)
		(ego get: iTestObject)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
	)
	
	(method (handleEvent event)
		(super handleEvent: event)
		; handle Said's, etc...
	)
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
		)
	)
)
