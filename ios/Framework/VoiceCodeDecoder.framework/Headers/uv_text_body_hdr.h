#ifndef	_UV_TEXT_BODY_HDR_H_
#define	_UV_TEXT_BODY_HDR_H_

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
/*  NAME      = uv_text_body_hdr.h					      */
/*  FUNC      = body header of Uni-Voice Unicode text body header	      */
/*  SYSTEM_ID = Uni-Voice						      */
/*  DATE      = 2015/08/15 by imashiki ; created			      */
/*		2016/09/01 by yamasaki ; added crypt related data types	      */
/*		2017/07/13 by yamasaki ; changed UV_BODY_SIZE 10240->20480    */
/*		2020/08/17 by yamasaki ; added lang type(code id)	      */
/******************************************************************************/

/* $Id$ */

#include "mdep_def.h"

/*
 * parameters
 */
#define UV_BODY_SIZE		20480		/* max body size */
#define UV_BODY_LANG_LEN	3		/* lauguage length */

/*
 * body header for Uni-Voice Unicode text code body
 *
 * 	Note: uvtb_size is set with little-endian encoding with 256 modulus.
 *
 * 	Body data format is as follows:
 *
 * 	    hdr1(t1,n1) data1(n1) hdr2(t2,n2) data(n2) ... UVB_TYPE_END(255) 
 *
 *      where hdrX(tX,nX) is a body header with uv_text_body_hdr_t type, and
 *      tX = hdrX.uvtb_type, nX = hdrX.uvtb_size[1] * 256 + hdrX.uvtb_size[0], 
 *      and data(nX) is nX bytes data with tX type. UVB_TYPE_END(255) is used
 *      to indicate end of text body data at the end of the text body without
 *      uvtb_lang and uvtb_size fields of uv_text_body_hdr_t.
 */
typedef struct uv_text_body_hdr {
	unsigned char	uvtb_type;		/* data type and attribute */
	unsigned char	uvtb_lang[UV_BODY_LANG_LEN]; /* language code */
	unsigned char	uvtb_size[2];		/* text body size */
} uv_text_body_hdr_t;

/* uvtb_type */
#define UVTB_TYPE_VAL(uvtb_type)	((uvtb_type) & 0x0f)	/* data type */
#define UVTB_TYPE_ATTR(uvtb_type)	((uvtb_type) & 0xf0)	/* data attr */

#define UVTB_TYPE_BIN		0		/* binary data */
#define UVTB_TYPE_SJIS		1		/* SJIS data */
#define UVTB_TYPE_UTF8		2		/* UTF8 data */
#define UVTB_TYPE_UTF16		3		/* UTF16-LE data */
#define UVTB_TYPE_ASCII		4		/* ASCII(ISO/IEC-8859-1) */
#define UVTB_TYPE_KEY		5		/* crypt key */
#define UVTB_TYPE_ENCRYPT	6		/* encrypted data */
#define UVTB_TYPE_DECRYPT	7		/* decrypted data */

#define UVTB_TYPE_CODEID	10		/* code id data */
#define UVTB_TYPE_END		255		/* end of body data */

#define UVTB_TYPE_ATTR_NO_SPEAK	0x80		/* no speak attribute */
#define UVTB_TYPE_ATTR_NO_SAVE	0x40		/* no save attribute */
#define UVTB_TYPE_ATTR_USED	0xc0		/* used attribute bits */

#define UVTB_TYPE_IS_CRYPT(data_type)		/* crypt related data type */ \
	((data_type) >= UVTB_TYPE_KEY && (data_type) <= UVTB_TYPE_DECRYPT)

#endif	/* _UV_TEXT_BODY_HDR_H_ */
