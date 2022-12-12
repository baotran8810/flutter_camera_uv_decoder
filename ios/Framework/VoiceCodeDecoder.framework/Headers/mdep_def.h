#ifndef	_MDEP_DEF_H_
#define	_MDEP_DEF_H_

/******************************************************************************/
/*                                                                            */
/* Copyright(C) 2016 Grape Systems, Inc.,  All Rights Reserved.               */
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
/*  NAME      = mdep_def.h						      */
/*  FUNC      = platform/machine dependent definitions			      */
/*  SYSTEM_ID = Uni-Voice						      */
/*  DATE      = 2016/09/28 by yamasaki ; created			      */
/******************************************************************************/

#ifdef WIN32
#define		CALLING_CONVENTION	__stdcall
#else	/* WIN32 */
#define		CALLING_CONVENTION
#endif	/* WIN32 */

#ifdef JCL_IMPORT
#define		DECLSPEC_VAR	__declspec(dllimport)
#else	/* JCL_IMPORT */
#define		DECLSPEC_VAR
#endif	/* JCL_IMPORT */

#ifdef WIN_VS
#define snprintf	_snprintf
#endif	/* WIN_VS */

#endif	/* _MDEP_DEF_H_ */
