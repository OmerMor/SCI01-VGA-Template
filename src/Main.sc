;;; Sierra Script 1.0 - (do not remove this comment)
(script# MAIN)
(include system.sh) (include sci2.sh) (include game.sh)
(use Intrface)
(use LoadMany)
(use Motion)
(use StopWalk)
(use Sound)
(use Save)
(use Game)
(use User)
(use Invent)
(use Menu)
(use System)

(public
	SCI01 0
	SetUpEgo 1	
	UnlockEgo 2
	LockEgo 3
	DisposeDialog 4
	Btst 5
	Bset 6
	Bclr 7

)

(local
	ego
	theGame
	curRoom
	speed =  6
	quit
	cast
	regions
	timers
	sounds
	inventory
	addToPics
	curRoomNum
	prevRoomNum
	newRoomNum
	debugOn
	score
	possibleScore
	showStyle =  7
	overRun
	theCursor
	normalCursor =  ARROW_CURSOR
	waitCursor =  HAND_CURSOR
	userFont =  USERFONT
	smallFont =  4
	lastEvent
	modelessDialog
	bigFont =  USERFONT
	global27 =  12
	version
	theDoits
	curSaveDir
	global31
	global32
	global33
	global34
	global35
	global36
	global37
	global38
	global39
	global40
	global41
	global42
	global43
	global44
	global45
	global46
	global47
	global48
	global49
	animationDelay =  10
	perspective
	features
	saidFeatures
	useSortedFeatures
	isDemoGame
	egoBlindSpot
	overlays =  -1
	doMotionCue
	systemWindow
	global60 =  3
	defaultPalette
	modelessPort
	sysLogPath
	global64
	global65
	global66
	global67
	global68
	global69
	global70
	global71
	global72
	ftrInitializer
	doVerbCode
	approachCode
	useObstacles =  TRUE
	global77
	global78
	global79
	global80
	global81
	global82
	global83
	global84
	global85
	global86
	global87
	global88
	global89
	global90
	global91
	global92
	global93
	global94
	global95
	global96
	global97
	global98
	global99
	isEgoLocked
	deathSound	= sDeath
	musicChannels
	global103
	testingCheats
	detailLevel
	music
	colorCount
	speedCount
	global109
	SFX
	gameFlags	;each global can have 16 flags.
	global112
	global113
	global114
	global115
	global116
	global117
	global118
	global119
	global120	;10 globals * 16 flags = 160 flags. If you need more flags, just add more globals!
)

(procedure (SetUpEgo)
	(Animate (cast elements?) 0)
)

(procedure (UnlockEgo)
	(= isEgoLocked FALSE)
	(User canControl: TRUE canInput: TRUE)
	(theGame setCursor: normalCursor (HaveMouse))
)

(procedure (LockEgo)
	(= isEgoLocked TRUE)
	(User canControl: FALSE canInput: FALSE)
	(theGame setCursor: waitCursor TRUE)
	(ego setMotion: 0)
)

(procedure (DisposeDialog)
	(if modelessDialog (modelessDialog dispose:))
)

(procedure (Btst flagEnum)
   (& [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
)

(procedure (Bset flagEnum  &tmp oldState)
   (= oldState (Btst flagEnum))
   (|= [gameFlags (/ flagEnum 16)] (>> $8000 (mod flagEnum 16)))
   oldState
)

(procedure (Bclr flagEnum  &tmp oldState)
   (= oldState (Btst flagEnum))
   (&= [gameFlags (/ flagEnum 16)] (~ (>> $8000 (mod flagEnum 16))))
   oldState
)


(instance egoObj of Ego
	(properties)
)

(instance statusCode of Code
	(properties)
	
	(method (doit str)
		(Format str 0 0 score possibleScore) ;Note: This is stored in text.000 to allow for the title and score to be aligned properly.
	)
)

(instance GlobalMusic of Sound
	(properties
		number 10
	)
)
(instance egoSW of StopWalk
	(properties)
)
(instance Test_Object of InvI
	(properties
		name {Test Object}
		description {This is a test object.}
		owner 0
		view 800
		loop 0
		cel 0
	)
)

(instance miscMusic of Sound
	(properties
		number 10
		priority 15
	)
)

(instance SCI01 of Game
	(properties)
	
	(method (init)
		(= testingCheats ENABLED) ;Set to ENABLED if you want to enable the debug features.
		(SysWindow color: BLACK back: vWHITE) ;These colors can be changed to suit your preferences.
		(= colorCount (Graph GDetect))
		(= systemWindow SysWindow)
		(super init:)
		(= musicChannels (DoSound NumVoices))
		(= ego egoObj)
		(User alterEgo: ego)
		(ego setCycle: StopWalk 1)
		(= showStyle 0)
		(TheMenuBar init: hide:)
		(StatusLine code: statusCode enable:)
		(StopWalk init:)
		(if testingCheats
			(self setCursor: normalCursor (HaveMouse) 300 170)
		else
			(LockEgo)
			(self setCursor: normalCursor 0 350 200)
		)
		((= music GlobalMusic) number: 10 owner: self init:)
		((= SFX miscMusic) number: 10 owner: self init:)
		(inventory add:
				Test_Object
		)
		(self newRoom: 99)
	)
	
	(method (doit)
		(super doit:)
	)
	
	(method (startRoom param1 &tmp temp0)
		(LoadMany FALSE EXTRA QSOUND GROOPER FORCOUNT SIGHT DPATH)
		(if testingCheats
			(if
				(and
					(u> (MemoryInfo 1) (+ 20 (MemoryInfo 0)))
					(Print {Memory fragmented.} #button {Debug} 1)
				)
				(SetDebug)
			)
			(User canInput: TRUE)
		)
		(super startRoom: param1)
	)
	
	(method (handleEvent event)
		(if testingCheats
			(if
				(and
					(== (event type?) evMOUSEBUTTON)
					(& (event modifiers?) emSHIFT)
				)
				(if (not (User canInput:))
					(event claimed: 1)
				else
					(cast eachElementDo: #handleEvent event)
					(if (event claimed?) (return))
				)
			)
			(super handleEvent: event)
			(if (event claimed?) (return))
			(switch (event type?)
				(evKEYBOARD
					((ScriptID 800) handleEvent: event)
				)
				(evMOUSEBUTTON
					((ScriptID 800) handleEvent: event)
				)
			)
		else
			(super handleEvent: event)
		)
	)
)
