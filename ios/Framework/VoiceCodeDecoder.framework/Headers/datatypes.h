// -*-  Mode:ObjC; c-basic-offset:4; tab-width:8; indent-tabs-mode:nil -*-
/*
プロジェクト名：音声コードデコーダライブラリ
ファイル名　　：datatypes.h

  $Id$

  (C)Japan Association for the Visually-impaired Information Support
     All Rights Reserved.
*****************************************************************************
本製品に関するすべての権利は日本視覚障がい情報普及支援協会に属します。
当協会の書面による許可を得ずに、本製品の全部または一部を複製することを禁じます。
*****************************************************************************
*/
#ifndef DATATYPES_H__
#define DATATYPES_H__

#include <assert.h>

/************************************/
/* Data Types                       */
/************************************/

/* general-purpose data types */

typedef signed char B;      /* signed 8-bit integer */
typedef long W;             /* signed 32-bit integer */
typedef unsigned char UB;   /* unsigned 8-bit integer */
typedef unsigned long UW;   /* unsigned 32-bit integer */
typedef unsigned long ulong;
typedef char VB;            /* unpredictable data type (8-bit size) */
typedef long VW;            /* unpredictable data type (32-bit size) */
typedef void *VP;           /* pointer to an unpredictable data type */
typedef void (*FP)();       /* program start address */
                            /* 32-bit CPU */
typedef short H;            /* signed 16-bit integer */
typedef unsigned short UH;  /* unsigned 16-bit integer */
typedef unsigned short ushort;

typedef int INT;
typedef unsigned int UINT;
typedef unsigned int uint;

typedef unsigned char uchar;

#define SUCCESS		(0)
#define FAILURE		(-1)

#define PI 3.1415926535897932f

#define BYTE_SIZE			(8)
#define WORD_SIZE			(11)
#define HEADER_BYTE_SIZE		(11)
#define HEADER_WORD_SIZE		(8)
#define HEADER_DATA_SIZE_POS		(9)
#define ALIGNMENT_1			(4)
#define ALIGNMENT_2			(3)

#define MAX_DATA_SIZE			(102400)
#define MAX_DATA_CODE			(1024)
#define MAX_HEADER_BYTE_SIZE	(22)
#define MAX_HEADER_WORD_SIZE	(16)
#define MAX_BIT_SIZE			(19600)
#define MAX_STRING			(128)
#define MAX_BITMAP_SIZE			(140)
#define MAX_ALIGNMENT			(ALIGNMENT_1+ALIGNMENT_2)
#define MAX_ARCHIVE			100	// JAVIS要望値100、メモリ不足が片付くまで制限
#define MAX_SIDEEDGES 			(20)
#define MAX_EDGEPOINTS			(100)

#define MAX_REG_POINTS   		200 //(550) // TODO 要調整
#define MAX_REGS			5120
#define MAX_MARKER_POINTS		100

#define MAX_SCALE 			(16)
#define MAX_VERIFY_REGS 		(20)
#define GF				(2048)

#define TYPE_TEXT		'T'
#define TYPE_DICTIONARY		'D'
#define TYPE_BINARY		'B'
#define TYPE_MOVIE		'M'
#define TYPE_CONTROL		'C'
#define TYPE_ENGLISH		'E'

#define RECOVER_NORMAL		'N'
#define RECOVER_STRONG		'S'
#define RECOVER_POOR		'P'
#define CELL_S			'S'
#define CELL_M			'M'
#define CELL_L			'L'

typedef struct _sPoint
{
    short x;
    short y;
} sPoint;

typedef struct _sClosedRegion
{
    int PointsCount;	
    int sumx;
    int sumy;
    int maxx;
    int minx;
    int maxy;
    int miny;
    int Side;  //left..right, top or bottom 
    sPoint Centroid;
} sClosedRegion;

typedef struct _Variable_struct
{
    int left_mkcount;
    int right_mkcount;
    int top_mkcount;
    int bottom_mkcount;
    int num_row_block;
    int num_col_block;
    sPoint SA[MAX_SCALE];
    sPoint SB[MAX_SCALE];
    sPoint SC[MAX_SCALE];
    sPoint SD[MAX_SCALE];
    float angle;
    int Orientation;
} Variable_struct;

//Grape Univoiceテキスト圧縮対応
//Univoice Text Header
typedef struct TagUVTextHeader
{
	unsigned char  dataType;
	unsigned char  country[3];
	unsigned short length;
} UVTextHeader;

#if linux || WIN32
#define ASSERT(x) assert(x)
#else
#define ASSERT(x) { if (!(x)) AssertFail(); }
#endif

#define MAX(x, y) ((x) > (y)) ? (x) : (y)
#define MIN(x, y) ((x) < (y)) ? (x) : (y)

#endif /* DATATYPES_H_ */
