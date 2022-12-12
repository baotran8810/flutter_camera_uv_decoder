#ifndef	_UV_SYM_PARAM_H_
#define	_UV_SYM_PARAM_H_

/******************************************************************************/
/*                                                                            */
/* Copyright(C) 2015 Grape Systems, Inc.,  All Rights Reserved.               */
/*                                                                            */
/* This software is furnished under a license, and may be used only in        */
/* accordance with the terms of such license and with the inclusion of the    */
/* above copyright notice.  No title to and the ownership of the software     */
/* is transferred.                                                            */
/* Grape Systems Inc. makes no representation or warranties with respect to   */
/* non-infringement of any intellectual property rights and the performance   */
/* of this computer program, and specifically disclaims any responsibility    */
/* for any damages, special or consequential, connected  with the use of      */
/* this program.                                                              */
/******************************************************************************/
/******************************************************************************/
/*  NAME      = uv_sym_param.h						      */
/*  FUNC      = user specified parameters for Uni-Voice symbol		      */
/*  SYSTEM_ID = Uni-Voice						      */
/*  DATE      = 2015/08/15 by imashiki ; created			      */
/*		2017/07/18 by yamasaki ; added symbol size "XL"		      */
/******************************************************************************/

/* $Id$ */

#include "mdep_def.h"

/*
 * parameters
 */
#define UVP_SYM_RSV_SZ	16			/* reserved sym_param size */

/*
 * user specified parameter for Uni-Voice symbol
 */
typedef struct uv_sym_param {
	int		sym_size;		/* symbol size */
	int		sym_body_type;		/* symbol body data type */
	int		sym_ec_level;		/* error correct level */
	char		sym_rsv[UVP_SYM_RSV_SZ];/* reserved */
} uv_sym_param_t;

/* sym_size */
#define UVP_SYM_SIZE_UNKNOWN	-1		/* unknown size */
#define UVP_SYM_SIZE_XS		0		/* extra small size */
#define UVP_SYM_SIZE_S		1		/* small size */
#define UVP_SYM_SIZE_M		2		/* medium size */
#define UVP_SYM_SIZE_L		3		/* large size */
#define UVP_SYM_SIZE_XL		4		/* extra large size */

#define UVP_SYM_SIZE_DEF	UVP_SYM_SIZE_M	/* default size */
#define UVP_SYM_SIZE_MAX	UVP_SYM_SIZE_XL	/* max parameter value */

/* sym_body_type */
#define UVP_SYM_BODY_T_UNKNOWN	-1		/* unknown body type */
#define UVP_SYM_BODY_T_JVT	0		/* JAVIS SJIS text */
#define UVP_SYM_BODY_T_UVT	1		/* Uni-Voice Unicode text */

#define UVP_SYM_BODY_T_OTH	256		/* other SJIS text  */
						/* decode case only */

#define UVP_SYM_BODY_T_DEF	UVP_SYM_BODY_T_JVT /* default body type */
#define UVP_SYM_BODY_T_MAX	UVP_SYM_BODY_T_UVT /* max parameter value */

/* sym_ec_evel */
#define UVP_SYM_ECL_UNKNOWN	-1		/* unknown level */
#define UVP_SYM_ECL_L		0		/* low error correct level */
#define UVP_SYM_ECL_M		1		/* middle error correct level */
#define UVP_SYM_ECL_H		2		/* high error correct level */

#define UVP_SYM_ECL_DEF		UVP_SYM_ECL_M	/* default level */
#define UVP_SYM_ECL_MAX		UVP_SYM_ECL_H	/* max parameter value */

#endif	/* _UV_SYM_PARAM_H_ */
