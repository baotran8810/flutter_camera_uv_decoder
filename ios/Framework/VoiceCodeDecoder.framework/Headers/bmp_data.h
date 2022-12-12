#ifndef	_BMP_DATA_H_
#define	_BMP_DATA_H_

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
/*  NAME      = bmp_data.h						      */
/*  FUNC      = definitions for common bitmap data			      */
/*  SYSTEM_ID = Uni-Voice						      */
/*  DATE      = 2015/08/15 by imashiki ; created			      */
/******************************************************************************/

/* $Id$ */

#include "mdep_def.h"

/*
 * bitmap data
 */
typedef struct bmp_data bmp_data_t;		/* bitmap data */
struct bmp_data {
	unsigned char	bmp_type;		/* bitmap data type */
	unsigned char	bmp_alloc;		/* allocate status */
	int		bmp_bits;		/* bits per pixcel */
	int		bmp_width;		/* horizontal image pixels */
	int		bmp_height;		/* virtical image pixels */
	int		bmp_line;		/* line byte size */
	int		bmp_ref_cnt;		/* reference count */
	int		bmp_data_size;		/* data size */
	int		bmp_buf_size;		/* buffer size */
	unsigned char	*bmp_buf;		/* bitmap data buffer */
	bmp_data_t	*bmp_ref;		/* referring bitmap */
};

/* bmp_type */

#define BMP_TYPE_NONE		0		/* no data */

/* 0/1 processing input/output bitmap data */
#define BMP_TYPE_01		1		/* output of 0/1 processing */
#define BMP_TYPE_DIGIT		2		/* digitized bitmap data */
#define BMP_TYPE_GRAY		3		/* gray scale byte data */

/* intermidiate bitmap data read from windows bitmap or YUV file */
#define BMP_TYPE_RGB		4		/* RGB BMP data (BGR) */
#define BMP_TYPE_REV_RGB	5		/* upside down RGB BMP data */
#define BMP_TYPE_PALET		6		/* color palette RGB data */
#define BMP_TYPE_REV_PALET	7		/* upsided down palette RGB */
#define BMP_TYPE_WBMP		8		/* windows BMP file data */
#define BMP_TYPE_UYVY		9		/* YUV422 UYVY type data */
#define BMP_TYPE_YUYV		10		/* YUV422 YUYV type data */

/* bmp_alloc */
#define BMP_ALLOC_NONE		0		/* not allocated */
#define BMP_ALLOC_DYN		1		/* dynamically allocated */
#define BMP_ALLOC_STAT		2		/* statically allocated */
#define BMP_ALLOC_REF		3		/* refer to other buffer */

/*
 * bitmap point X,Y coordinate
 */
typedef struct bmp_point {
	int		bmp_pt_x;		/* X coordinate */
	int		bmp_pt_y;		/* Y coordinate */
} bmp_point_t;

/*
 * bitmap area
 */
typedef struct bmp_area {
	int		bmp_ar_x;		/* X coordinate */
	int		bmp_ar_y;		/* Y coordiante */
	int		bmp_ar_width;		/* horizontal width */
	int		bmp_ar_height;		/* virtical height */
} bmp_area_t;

/*
 * exported interfaces
 */
int CALLING_CONVENTION	bmp_check_and_alloc_buf(bmp_data_t *bmp, int size);
						/* check and allocate buffer */
void CALLING_CONVENTION	bmp_ref_bmp(bmp_data_t *bmp, bmp_data_t *ref_bmp);
						/* refer to other bitmap */
void CALLING_CONVENTION	bmp_free_buf(bmp_data_t *bmp);
						/* free allocated buffer */

#endif	/* _BMP_DATA_H_ */
