;;; Sierra Script 1.0 - (do not remove this comment)
(script# 995)
(include sci.sh)
(use Main)
(use Intrface)
(use Save)
(use System)


(local
	newDButton
)
(class InvI of Object
	(properties
		said 0
		description 0
		owner 0
		view 0
		loop 0
		cel 0
		script 0
	)
	
	(method (showSelf)
		(PrintD (if description else name) view loop cel)
	)
	
	(method (saidMe)
		(Said said)
	)
	
	(method (ownedBy param1)
		(return (== owner param1))
	)
	
	(method (moveTo theOwner)
		(= owner theOwner)
		(return self)
	)
	
	(method (changeState newState)
		(if script (script changeState: newState))
	)
)

(class Inv of Set
	(properties
		elements 0
		size 0
		carrying {You are carrying:}
		empty {You are carrying nothing!}
	)
	
	(method (init)
		(= inventory self)
	)
	
	(method (showSelf param1)
		(invD text: carrying doit: param1)
	)
	
	(method (saidMe)
		(self firstTrue: #saidMe)
	)
	
	(method (ownedBy param1)
		(self firstTrue: #ownedBy param1)
	)
)

(instance invD of Dialog
	(properties)
	
	(method (init param1 &tmp temp0 temp1 temp2 temp3 newDText inventoryFirst temp6)
		(= temp2 (= temp0 (= temp1 4)))
		(= temp3 0)
		(= inventoryFirst (inventory first:))
		(while inventoryFirst
			(if
			((= temp6 (NodeValue inventoryFirst)) ownedBy: param1)
				(++ temp3)
				(self
					add:
						((= newDText (DText new:))
							value: temp6
							text: (temp6 name?)
							nsLeft: temp0
							nsTop: temp1
							state: 3
							font: smallFont
							setSize:
							yourself:
						)
				)
				(if
				(< temp2 (- (newDText nsRight?) (newDText nsLeft?)))
					(= temp2 (- (newDText nsRight?) (newDText nsLeft?)))
				)
				(if
					(>
						(= temp1
							(+ temp1 (- (newDText nsBottom?) (newDText nsTop?)) 1)
						)
						140
					)
					(= temp1 4)
					(= temp0 (+ temp0 temp2 10))
					(= temp2 0)
				)
			)
			(= inventoryFirst (inventory next: inventoryFirst))
		)
		(if (not temp3) (self dispose:) (return 0))
		(= window SysWindow)
		(self setSize:)
		(= newDButton (DButton new:))
		(newDButton
			text: {OK}
			setSize:
			moveTo: (- nsRight (+ 4 (newDButton nsRight?))) nsBottom
		)
		(newDButton
			move: (- (newDButton nsLeft?) (newDButton nsRight?)) 0
		)
		(self add: newDButton setSize: center:)
		(return temp3)
	)
	
	(method (doit param1 &tmp theNewDButton)
		(if (not (self init: param1))
			(Print (inventory empty?))
			(return)
		)
		(self open: 4 15)
		(= theNewDButton newDButton)
		(repeat
			(if
				(or
					(not (= theNewDButton (super doit: theNewDButton)))
					(== theNewDButton -1)
					(== theNewDButton newDButton)
				)
				(break)
			)
			((theNewDButton value?) showSelf:)
		)
		(self dispose:)
	)
	
	(method (handleEvent pEvent &tmp pEventMessage pEventType)
		(= pEventMessage (pEvent message?))
		(switch (= pEventType (pEvent type?))
			(4
				(switch pEventMessage
					(KEY_UP (= pEventMessage 3840))
					(KEY_NUMPAD2
						(= pEventMessage 9)
					)
				)
			)
			(64
				(switch pEventMessage
					(JOY_UP
						(= pEventMessage 3840)
						(= pEventType 4)
					)
					(JOY_DOWN
						(= pEventMessage 9)
						(= pEventType 4)
					)
				)
			)
		)
		(pEvent type: pEventType message: pEventMessage)
		(super handleEvent: pEvent)
	)
)
