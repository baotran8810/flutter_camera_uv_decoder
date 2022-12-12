#ifndef	_UV_ERROR_H_
#define	_UV_ERROR_H_

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
/*  NAME      = uv_error.h						      */
/*  FUNC      = definitions for error numbers				      */
/*  SYSTEM_ID = Uni-Voice						      */
/*  DATE      = 2015/08/15 by imashiki ; created			      */
/*		2016/07/22 by yamasaki ; added error numbers(80-83)	      */
/*		2017/08/25 by imashiki : added error numbers(73-74,84-89)     */
/******************************************************************************/

/* $Id$ */

#include "mdep_def.h"

/*
 * error numbers
 */
#define	UV_ERR_OPEN		-1		/* failed to open file */
#define UV_ERR_CREAT		-2		/* failed to create file */
#define UV_ERR_READ		-3		/* failed to read file */
#define UV_ERR_WRITE		-4		/* failed to write file */
#define UV_ERR_MEM		-5		/* failed to allocate memory */
#define UV_ERR_BIG		-6		/* too big data */
#define UV_ERR_FORM		-7		/* invalid data format */
#define UV_ERR_UNSUP		-8		/* unsupported type */
#define UV_ERR_TYPE		-9		/* data type mismatch */
#define UV_ERR_PARAM		-10		/* invalid parameter */
#define UV_ERR_SMALL		-11		/* too small image data */
#define UV_ERR_T_MARK		-12		/* unrecognized T-mark */
#define UV_ERR_BLK_CNT		-13		/* invalid block count */
#define UV_ERR_BLK_UNMATCH	-14		/* side block count unmatch */
#define UV_ERR_ROTATE		-15		/* not rotated symbol */
#define UV_ERR_NO_HDR		-16		/* no header data */
#define UV_ERR_CORRECT_HDR	-17		/* uncorrectable error (hdr) */
#define UV_ERR_CORRECT		-18		/* uncorrectable error (body) */
#define UV_ERR_HDR_TYPE		-19		/* bad header (type value) */
#define UV_ERR_HDR_REV		-20		/* bad header (revision) */
#define UV_ERR_HDR_STUFF	-21		/* bad header (stuff method) */
#define UV_ERR_HDR_ID		-22		/* bad header (license ID) */
#define UV_ERR_HDR_WD_CNT	-23		/* bad header (word count) */
						/* data/redundant word count */
#define UV_ERR_OVER_BUF		-24		/* over body buffer */
#define UV_ERR_OVER_BODY	-25		/* over body data */
#define UV_ERR_COMP_DATA	-26		/* invalid compressed data */
#define UV_ERR_OVER_DECOMP	-27		/* over decompressed data */
#define UV_ERR_UNSUP_TEXT	-28		/* unsupported text data */
#define UV_ERR_PACK_DATA	-29		/* invalid packed data */

#define UV_ERR_OVER_TEXT	-30		/* over original text data */
#define UV_ERR_PARAM_SIZE	-31		/* invalid symbol size param */
#define UV_ERR_PARAM_TYPE	-32		/* invalid body type param */
#define UV_ERR_PARAM_ECL	-33		/* invalid error correct */
						/* level param           */
#define UV_ERR_PARAM_DPI	-34		/* invalid DPI param */
#define UV_ERR_PARAM_CELL_SZ	-35		/* invalid cell size param */
#define UV_ERR_PARAM_SHAPE	-36		/* invalid cell shape param */
#define UV_ERR_PARAM_BLK_LEV	-37		/* invalid black level param */
#define UV_ERR_OVER_SYM_SIZE	-38		/* over symbol size */

#define UV_ERR_LANG_DEF_FILE	-50		/* invalid language def file */
#define UV_ERR_LANG_NAME_DEF	-51		/* invalid language name def */
#define UV_ERR_LANG_TYPE_DEF	-52		/* invalid body type def */
#define UV_ERR_LANG_EXT_DEF	-53		/* extra data after def */
#define UV_ERR_LANG_MANY_DEF	-54		/* too many language def */
#define UV_ERR_TAG_ERR_COM	-55		/* error in comment */
#define UV_ERR_TAG_EXT_COM	-56		/* extra data after comment */
#define UV_ERR_TAG_NO_START	-57		/* no start tag */
#define UV_ERR_TAG_UNKNOWN	-58		/* unknown start tag */
#define UV_ERR_TAG_EOF_TAG	-59		/* unexpected EOF in <tag ..> */
#define UV_ERR_TAG_LANG_ATTR    -60		/* invalid language attribute */
#define UV_ERR_TAG_SIZE_ATTR	-61		/* invalid size attribute */
#define UV_ERR_TAG_TYPE_ATTR	-62		/* invalid type attribute */
#define UV_ERR_TAG_DELNL_ATTR	-63		/* invalid del_nl attribute */
#define UV_ERR_TAG_ATTR		-64		/* invalid tag attribute */
#define UV_ERR_TAG_NO_LANG	-65		/* no language attribute */
#define UV_ERR_TAG_EXT_TAG	-66		/* extra data after <tag ..> */
#define UV_ERR_TAG_OVER_TEXT	-67		/* over tagged text data */
#define UV_ERR_TAG_MID_LINE	-68		/* data ends in mid line */
#define UV_ERR_TAG_NO_END	-69		/* no end tag */
#define UV_ERR_TAG_NO_DATA	-70		/* no data */
#define UV_ERR_TAG_BAD_CHAR	-71		/* invalid character in data */
#define UV_ERR_TAG_OVER_BUF	-72		/* over buffer */
#define UV_ERR_TAG_HEX_ATTR	-73		/* invalid hex attribute */
#define UV_ERR_TAG_ATTR_ATTR	-74		/* invalid attr attribute */

#define UV_ERR_DECRYPT		-80		/* decrypt error */
#define UV_ERR_KEY_DECRYPT	-81		/* key file decrypt error */
#define UV_ERR_INV_DEC_DATA	-82		/* invalid decoded data */
#define UV_ERR_ENCRYPT		-83		/* encrypt error */
#define UV_ERR_TAG_CRYPT_KEY_ID	-84		/* invalid crypt key id */
#define UV_ERR_TAG_CRYPT_TYPE	-85		/* invalid crypt data type */
#define UV_ERR_TAG_CRYPT_LANG	-86		/* invalid crypt language */
#define UV_ERR_TAG_CRYPT_KEY	-87		/* invalid crypt key */
#define UV_ERR_TAG_CRYPT_DATA	-88		/* invalid crypt data */
#define UV_ERR_TAG_CRYPT_EXPIRE	-89		/* invalid crypt expire info */

#define UV_ERR_NOT_PROC		-99		/* not proccessed phase */

#endif	/* _UV_ERROR_H_ */
