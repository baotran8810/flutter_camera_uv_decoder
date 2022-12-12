#ifndef	_UV_DECODE_SYM_H_
#define	_UV_DECODE_SYM_H_

/******************************************************************************/
/*																			  */
/* Copyright(C) 2015 Grape Systems, Inc.,  All Rights Reserved. 			  */
/*																			  */
/* This software is furnished under a license, and may be used only in		  */
/* accordance with the terms of such license and with the inclusion of the	  */
/* above copyright notice.	No title to and the ownership of the software	  */
/* is transferred.															  */
/* Grape Systems Inc. makes no representation or warranties with respect to   */
/* non-infringement of any intellectual property rights and the performance   */
/* of this computer program, and specifically disclaims any responsibility	  */
/* for any damages, special or consequential, connected  with the use of	  */
/* this program.															  */
/******************************************************************************/
/******************************************************************************/
/*	NAME	  = uv_decode_sym.h							  */
/*	FUNC	  = decode Uni-Voice symbol						  */
/*	SYSTEM_ID = Uni-Voice							  */
/*	DATE	  = 2015/08/15 by imashiki ; created				  */
/*		2016/09/01 by yamasaki : modified for encrypt/decrypt		  */
/*		2017/08/25 by imashiki ; added uv_decode_sym_mode		  */
/*		2017/08/25 by imashiki ; added xxx_with_attr function		  */
/******************************************************************************/

/* $Id$ */

#include "mdep_def.h"
#include "bmp_data.h"
#include "uv_sym_param.h"

/*
 * parameters
 */
#define UVE_RSV_SZ		64		/* reserved err_info size	 */

/*
 * error information
 */
typedef struct uv_mark_info {			/* T-mark information		 */
	int		uvm_line_type;		/* line type			 */
	int		uvm_mark_cnt;		/* T-mark count			 */
	int		uvm_len_err;		/* mark length error count	 */
	int		uvm_skip;		/* mark skip count		 */
	int		uvm_miss;		/* mark miss count		 */
} uv_mark_info_t;

/* uvt_line_type */
#define UV_M_LINE_UNKNOWN	-1		/* unknown line type */
#define	UV_M_LINE_LEFT		0		/* left line */
#define UV_M_LINE_BOTTOM	1		/* bottom line */
#define	UV_M_LINE_RIGHT		2		/* right line */
#define	UV_M_LINE_TOP		3		/* top line */

typedef struct uv_err_info {			/* error information		 */
	int		uve_phase;		/* error dectected phase	 */
	int		uve_load_bmp;		/* ret value of load bitmap  */
	int		uve_clip_sym;		/* ret value of clip symbol  */
	bmp_area_t	uve_area_info;		/* clipped area information  */
	unsigned char	uve_bk_v;		/* min black value		 */
	unsigned char	uve_wt_v;		/* max white value		 */
	int		uve_get_mark;		/* ret value of get T-mark	 */
	int		uve_rotate_cnt;		/* rotate count			 */
	bmp_point_t	uve_corner_info[4];	/* corner point information  */
	uv_mark_info_t	uve_mark_info[4];	/* T-mark information		 */
	int		uve_get_hdr;		/* ret value of get header	 */
	int		uve_use_copy;		/* used header copy or not	 */
	int		uve_check_hdr;		/* ret value of check header */
	int		uve_get_body;		/* ret value of get body	 */
	int		uve_decode_body;	/* ret value of decode body  */
	char		uve_rsv[UVE_RSV_SZ];	/* reserved			 */
} uv_err_info_t;

/* uve_phase */
#define UV_E_PHASE_NO_ERR	0		/* no error */
#define UV_E_PHASE_LOAD_BMP	1		/* loading bitmap */
#define UV_E_PHASE_CLIP_SYM	2		/* clipping symbol */
#define UV_E_PHASE_GET_MARK	3		/* get T-mark */
#define UV_E_PHASE_GET_HDR	4		/* get symbol header */
#define UV_E_PHASE_CHECK_HDR	5		/* check symbol header */
#define UV_E_PHASE_GET_BODY	6		/* get symbol body */
#define UV_E_PHASE_DECODE_BODY	7		/* decode body */

/* uve_load_bmp */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_OPEN		: failed to open BMP file			*/
/* UV_ERR_READ		: failed to read BMP file			*/
/* UV_ERR_MEM		: failed to allocate memory			*/
/* UV_ERR_BIG		: too big data to store in the buffer		*/
/* UV_ERR_UNSUP		: unsupported bit type of data			*/
/* UV_ERR_FORM		: invalid data format				*/
/* 0			: success to load				*/

/* uve_clip_sym */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_UNSUP		: unsupported bitmap data type			*/
/* UV_ERR_SMALL		: too small image				*/
/* UV_ERR_MEM		: failed to allocate work area for sub-area	*/
/* 0			: success to clip				*/

/* uve_corner_info */
#define UV_E_CORNER_LT		0		/* left-top */
#define UV_E_CORNER_LB		1		/* left-bottom */
#define UV_E_CORNER_RB		2		/* right-bottom */
#define UV_E_CORNER_RT		3		/* right-top */

/* uve_get_mark */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_T_MARK	: unrecognized T-mark				*/
/* UV_ERR_BLK_CNT	: invalid block count				*/
/* UV_ERR_BIG		: too big bitmap data				*/
/* UV_ERR_MEM		: failed to allocate work memory		*/
/* UV_ERR_ROTATE	: not rotated symbol				*/
/* UV_ERR_BLK_UNMATCH	: block count unmatch between sides		*/
/* 0		  	: success to get T-marks			*/

/* uve_rotate_cnt */
/* -1			: unknown					*/
/* 0			: no rotation					*/
/* 1			: +90 degree					*/
/* 2			: +180 degree					*/
/* 3			: +270 degree					*/

/* uve_get_hdr */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_NO_HDR	: no header data				*/
/* UV_ERR_CORRECT_HDR	: detected uncorrectable header error count	*/
/* UV_ERR_MEM		: failed to allocat work memory			*/
/* 0			: no error					*/
/* others		: corrected error word count			*/

/* uve_use_copy */
/* -1			: unknown					*/
/* 0			: not used copy					*/
/* 1			: used copy					*/

/* uve_check_hdr */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_HDR_TYPE	: invalid header type				*/
/* UV_ERR_HDR_REV	: invalid revision				*/
/* UV_ERR_HDR_STUFF	: invalid stuff method				*/
/* UV_ERR_HDR_ID	: invalid license id				*/
/* 0			: no error					*/

/* uve_get_body */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_HDR_WD_CNT	: invalid header data/redunant word count	*/
/* UV_ERR_CORRECT	: detected uncorrectable error in body		*/
/* UV_ERR_OVER_BUF	: over body buffer				*/
/* 0			: no error					*/
/* others		: corrected error word count			*/

/* uve_decode_body */
/* UV_ERR_NOT_PROC	: not processed phase				*/
/* UV_ERR_OVER_BODY	: over body data				*/
/* UV_ERR_COMP_DATA	: invalid compressed data			*/
/* UV_ERR_OVER_DECOMP	: over decompressed data			*/
/* UV_ERR_UNSUP_TEXT	: unsupported text body				*/
/* UV_ERR_PACK_DATA	: invalid packed data				*/
/* UV_ERR_OVER_BUF	: over decocde buffer				*/
/* UV_ERR_MEM		: failed to allocate work memory		*/
/* others		: decoded byte count				*/

#ifdef __cplusplus	// for iOS static library
extern "C" {		// for iOS static library
#endif				// for iOS static library

/*
 * exported interfaces
 */
int CALLING_CONVENTION uv_decode_sym_file(
		const char *bmp_file, uv_err_info_t *err_info,
		uv_sym_param_t *param, unsigned char *data_buf, int buf_size);
						/* decode symbol file */
int CALLING_CONVENTION uv_decode_sym_bmp(
		bmp_data_t *gray_bmp, int free_bmp, uv_err_info_t *err_info,
		uv_sym_param_t *param, unsigned char *data_buf, int buf_size);
						/* decode symbol bitmap data */

int CALLING_CONVENTION uv_decode_sym_file_with_attr(
		const char *bmp_file, uv_err_info_t *err_info,
		uv_sym_param_t *param, unsigned char *data_buf, int buf_size);
						/* decode symbo file */
						/* with attribute */
int CALLING_CONVENTION uv_decode_sym_bmp_with_attr(
		bmp_data_t *gray_bmp, int free_bmp, uv_err_info_t *err_info,
		uv_sym_param_t *param, unsigned char *data_buf, int buf_size);
						/* decode symbol bitmap */
						/* with attribute */

/*
 * exported variable
 */
extern int	uv_decode_sym_mode;		/* unknown data handling */

#ifdef __cplusplus	// for iOS static library
}					// for iOS static library
#endif				// for iOS static library

/* uv_decode_sym_mode */
#define UV_DECODE_SYM_MODE_SKIP		0	/* skip unknown */
#define UV_DECODE_SYM_MODE_ERR		1	/* return error for unknown */
#define UV_DECODE_SYM_MODE_RET		2	/* return unknown as it is */

#endif	/* _UV_DECODE_SYM_H_ */
