;;; Sierra Script 1.0 - (do not remove this comment)
(script# 990)
(include sci.sh)
(use Main)
(use Intrface)
(use System)

(public
	GetDirectory 0
)

(local
	default
	i
	numGames
	selected
	theStatus
	[butbuf1 4] = [{Restore} {__Save__} {Replace} {Replace}]
	[butbuf2 4] = [{Select the game that you would like to restore.} {Type the description of this saved game.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.} {This directory/disk can hold no more saved games. You must replace one of your saved games or use Change Directory to save on a different directory/disk.}]
)
(procedure (GetDirectory param1 &tmp temp0 [temp1 33] [temp34 40])
	(asm
code_000e:
		pushi    13
		pushi    990
		pushi    1
		pushi    33
		pushi    0
		pushi    41
		pushi    2
		lea      @temp1
		push    
		lsp      param1
		callk    StrCpy,  4
		push    
		pushi    29
		pushi    81
		lofsa    {OK}
		push    
		pushi    1
		pushi    81
		lofsa    {Cancel}
		push    
		pushi    0
		calle    Print,  26
		sat      temp0
		not     
		bnt      code_0047
		ldi      0
		ret     
code_0047:
		pushi    1
		lea      @temp1
		push    
		callk    StrLen,  2
		not     
		bnt      code_005c
		pushi    1
		lea      @temp1
		push    
		callk    GetCWD,  2
code_005c:
		pushi    1
		lea      @temp1
		push    
		callk    ValidPath,  2
		bnt      code_0077
		pushi    2
		lsp      param1
		lea      @temp1
		push    
		callk    StrCpy,  4
		ldi      1
		ret     
		jmp      code_000e
code_0077:
		pushi    3
		pushi    4
		lea      @temp34
		push    
		pushi    990
		pushi    2
		lea      @temp1
		push    
		callk    Format,  8
		push    
		pushi    33
		pushi    0
		calle    Print,  6
		jmp      code_000e
		ret     
	)
)

(procedure (localproc_009a)
	(return
		(cond 
			((== self Restore) 0)
			((localproc_00b9) 1)
			(numGames 2)
			(else 3)
		)
	)
)

(procedure (localproc_00b9)
	(if (< numGames 20) (CheckFreeSpace curSaveDir))
)

(procedure (localproc_00c7)
	(Print {You must type a description for the game.} #font 0)
)

(class SysWindow of Object
	(properties
		top 0
		left 0
		bottom 0
		right 0
		color 0
		back 15
		priority -1
		window 0
		type $0000
		title 0
		brTop 0
		brLeft 0
		brBottom 190
		brRight 320
	)
	
	(method (dispose)
		(if window (DisposeWindow window) (= window 0))
		(super dispose:)
	)
	
	(method (open)
		(= window
			(NewWindow
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
)

(class SRDialog of Dialog
	(properties
		elements 0
		size 0
		text 0
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
	
	(method (init param1 param2 param3)
		(= window SysWindow)
		(= nsBottom 0)
		(if
			(==
				(= numGames
					(GetSaveFiles (theGame name?) param2 param3)
				)
				-1
			)
			(return 0)
		)
		(if (== (= theStatus (localproc_009a)) 1)
			(editI
				text: (StrCpy param1 param2)
				font: smallFont
				setSize:
				moveTo: 4 4
			)
			(self add: editI setSize:)
		)
		(selectorI
			text: param2
			font: smallFont
			setSize:
			moveTo: 4 (+ nsBottom 4)
			state: 2
		)
		(= i (+ (selectorI nsRight?) 4))
		(okI
			text: [butbuf1 theStatus]
			setSize:
			moveTo: i (selectorI nsTop?)
			state: (if (== theStatus 3) 0 else 3)
		)
		(cancelI
			setSize:
			moveTo: i (+ (okI nsBottom?) 4)
			state: (& (cancelI state?) $fff7)
		)
		(changeDirI
			setSize:
			moveTo: i (+ (cancelI nsBottom?) 4)
			state: (& (changeDirI state?) $fff7)
		)
		(self add: selectorI okI cancelI changeDirI setSize:)
		(textI
			text: [butbuf2 theStatus]
			setSize: (- (- nsRight nsLeft) 8)
			moveTo: 4 4
		)
		(= i (+ (textI nsBottom?) 4))
		(self eachElementDo: #move 0 i)
		(self add: textI setSize: center: open: 4 15)
		(return 1)
	)
	
	(method (doit param1 &tmp temp0 temp1 temp2 temp3 [temp4 361] [temp365 21] [temp386 40])
		(asm
			pushSelf
			class    Restore
			eq?     
			bnt      code_02d0
			lap      argc
			bnt      code_02d0
			lap      param1
			bnt      code_02d0
			pushi    2
			pushi    0
			pushi    4
			lea      @temp386
			push    
			pushi    990
			pushi    0
			pushi    #name
			pushi    0
			lag      theGame
			send     4
			push    
			callk    Format,  8
			push    
			callk    FileIO,  4
			sat      temp1
			push    
			ldi      65535
			eq?     
			bnt      code_02c7
			ret     
code_02c7:
			pushi    2
			pushi    1
			lst      temp1
			callk    FileIO,  4
code_02d0:
			pushi    #init
			pushi    3
			lsp      param1
			lea      @temp4
			push    
			lea      @temp365
			push    
			self     10
			not     
			bnt      code_02e8
			ldi      65535
			ret     
code_02e8:
			lsl      theStatus
			dup     
			ldi      0
			eq?     
			bnt      code_02fe
			lal      numGames
			bnt      code_0317
			lofsa    okI
			jmp      code_0317
			lofsa    changeDirI
			jmp      code_0317
code_02fe:
			dup     
			ldi      1
			eq?     
			bnt      code_0309
			lofsa    editI
			jmp      code_0317
code_0309:
			dup     
			ldi      2
			eq?     
			bnt      code_0314
			lofsa    okI
			jmp      code_0317
code_0314:
			lofsa    changeDirI
code_0317:
			toss    
			sal      default
			pushi    #doit
			pushi    1
			push    
			super    Dialog,  6
			sal      i
			pushi    #indexOf
			pushi    1
			pushi    #cursor
			pushi    0
			lofsa    selectorI
			send     4
			push    
			lofsa    selectorI
			send     6
			sal      selected
			push    
			ldi      18
			mul     
			sat      temp3
			lsl      i
			lofsa    changeDirI
			eq?     
			bnt      code_03e9
			pushi    1
			lsg      curSaveDir
			call     GetDirectory,  2
			bnt      code_02e8
			pushi    3
			pushi    #name
			pushi    0
			lag      theGame
			send     4
			push    
			lea      @temp4
			push    
			lea      @temp365
			push    
			callk    GetSaveFiles,  6
			sal      numGames
			push    
			ldi      65535
			eq?     
			bnt      code_0379
			ldi      65535
			sat      temp2
			jmp      code_04ef
code_0379:
			lal      theStatus
			sat      temp0
			pushi    0
			call     localproc_009a,  0
			sal      theStatus
			push    
			dup     
			ldi      0
			eq?     
			bnt      code_038e
			jmp      code_03d7
code_038e:
			dup     
			lat      temp0
			eq?     
			bnt      code_03c1
			pushi    #contains
			pushi    1
			lofsa    editI
			push    
			self     6
			bnt      code_03d7
			pushi    #cursor
			pushi    1
			pushi    1
			pushi    2
			lsp      param1
			lea      @temp4
			push    
			callk    StrCpy,  4
			push    
			callk    StrLen,  2
			push    
			pushi    83
			pushi    0
			lofsa    editI
			send     10
			jmp      code_03d7
code_03c1:
			pushi    #dispose
			pushi    0
			pushi    102
			pushi    3
			lsp      param1
			lea      @temp4
			push    
			lea      @temp365
			push    
			self     14
code_03d7:
			toss    
			pushi    #setSize
			pushi    0
			pushi    83
			pushi    0
			lofsa    selectorI
			send     8
			jmp      code_02e8
code_03e9:
			lsl      theStatus
			ldi      2
			eq?     
			bnt      code_041f
			lsl      i
			lofsa    okI
			eq?     
			bnt      code_041f
			pushi    #doit
			pushi    1
			pushi    2
			lsp      param1
			lat      temp3
			leai     @temp4
			push    
			callk    StrCpy,  4
			push    
			lofsa    GetReplaceName
			send     6
			bnt      code_02e8
			lal      selected
			lati     temp365
			sat      temp2
			jmp      code_04ef
			jmp      code_02e8
code_041f:
			lsl      theStatus
			ldi      1
			eq?     
			bnt      code_0497
			lsl      i
			lofsa    okI
			eq?     
			bt       code_0436
			lsl      i
			lofsa    editI
			eq?     
			bnt      code_0497
code_0436:
			pushi    1
			lsp      param1
			callk    StrLen,  2
			push    
			ldi      0
			eq?     
			bnt      code_044c
			pushi    0
			call     localproc_00c7,  0
			jmp      code_02e8
code_044c:
			ldi      65535
			sat      temp2
			ldi      0
			sal      i
code_0454:
			lsl      i
			lal      numGames
			lt?     
			bnt      code_0474
			pushi    2
			lsp      param1
			lsl      i
			ldi      18
			mul     
			leai     @temp4
			push    
			callk    StrCmp,  4
			sat      temp2
			not     
			bnt      code_0470
code_0470:
			+al      i
			jmp      code_0454
code_0474:
			lat      temp2
			not     
			bnt      code_0480
			lal      i
			lati     temp365
			jmp      code_0490
code_0480:
			lsl      numGames
			ldi      20
			eq?     
			bnt      code_048e
			lal      selected
			lati     temp365
			jmp      code_0490
code_048e:
			lal      numGames
code_0490:
			sat      temp2
			jmp      code_04ef
			jmp      code_02e8
code_0497:
			lsl      i
			lofsa    okI
			eq?     
			bnt      code_04ab
			lal      selected
			lati     temp365
			sat      temp2
			jmp      code_04ef
			jmp      code_02e8
code_04ab:
			lsl      i
			ldi      0
			eq?     
			bt       code_04ba
			lsl      i
			lofsa    cancelI
			eq?     
			bnt      code_04c3
code_04ba:
			ldi      65535
			sat      temp2
			jmp      code_04ef
			jmp      code_02e8
code_04c3:
			lsl      theStatus
			ldi      1
			eq?     
			bnt      code_02e8
			pushi    #cursor
			pushi    1
			pushi    1
			pushi    2
			lsp      param1
			lat      temp3
			leai     @temp4
			push    
			callk    StrCpy,  4
			push    
			callk    StrLen,  2
			push    
			pushi    83
			pushi    0
			lofsa    editI
			send     10
			jmp      code_02e8
code_04ef:
			pushi    #dispose
			pushi    0
			self     4
			lat      temp2
			ret     
		)
	)
)

(class Restore of SRDialog
	(properties
		elements 0
		size 0
		text {Restore a Game}
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
)

(class Save of SRDialog
	(properties
		elements 0
		size 0
		text {Save a Game}
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		caller 0
		seconds 0
		lastSeconds 0
	)
)

(instance GetReplaceName of Dialog
	(properties)
	
	(method (doit param1 &tmp temp0)
		(= window SysWindow)
		(text1 setSize: moveTo: 4 4)
		(self add: text1 setSize:)
		(oldName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: oldName setSize:)
		(text2 setSize: moveTo: 4 nsBottom)
		(self add: text2 setSize:)
		(newName
			text: param1
			font: smallFont
			setSize:
			moveTo: 4 nsBottom
		)
		(self add: newName setSize:)
		(button1 nsLeft: 0 nsTop: 0 setSize:)
		(button2 nsLeft: 0 nsTop: 0 setSize:)
		(button2
			moveTo: (- nsRight (+ (button2 nsRight?) 4)) nsBottom
		)
		(button1
			moveTo: (- (button2 nsLeft?) (+ (button1 nsRight?) 4)) nsBottom
		)
		(self add: button1 button2 setSize: center: open: 0 15)
		(= temp0 (super doit: newName))
		(self dispose:)
		(if (not (StrLen param1))
			(localproc_00c7)
			(= temp0 0)
		)
		(return (if (== temp0 newName) else (== temp0 button1)))
	)
)

(instance selectorI of DSelector
	(properties
		x 36
		y 8
	)
)

(instance editI of DEdit
	(properties
		max 35
	)
)

(instance okI of DButton
	(properties)
)

(instance cancelI of DButton
	(properties
		text { Cancel_}
	)
)

(instance changeDirI of DButton
	(properties
		text {Change\0D\nDirectory}
	)
)

(instance textI of DText
	(properties
		font 0
	)
)

(instance text1 of DText
	(properties
		text {Replace}
		font 0
	)
)

(instance text2 of DText
	(properties
		text {with:}
		font 0
	)
)

(instance oldName of DText
	(properties)
)

(instance newName of DEdit
	(properties
		max 35
	)
)

(instance button1 of DButton
	(properties
		text {Replace}
	)
)

(instance button2 of DButton
	(properties
		text {Cancel}
	)
)
