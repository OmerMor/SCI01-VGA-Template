;;; Sierra Script 1.0 - (do not remove this comment)
(script# LOADMANY)
(include system.sh) (include sci2.sh)

(public
	LoadMany 0
)

(procedure (LoadMany what which &tmp i theRes)
	
	;;Author: Pablo Ghenis, 7/17/89
	;;
	;;Usage examples:
	;;       (LoadMany VIEW 110 111 201)   ;to load a series of views
	;;       (LoadMany SCRIPT 110 111 201) ;to load a series of scripts
	;;       (LoadMany FALSE 110 111 201)  ;to use DisposeScript

	(= argc (- argc 2))
	(= i 0)
	(while (<= i argc)
		(= theRes [which i])
		(if what
			(Load what theRes)
		else
			(DisposeScript theRes)
		)
		(++ i)
	)
	(DisposeScript LOADMANY)
)
