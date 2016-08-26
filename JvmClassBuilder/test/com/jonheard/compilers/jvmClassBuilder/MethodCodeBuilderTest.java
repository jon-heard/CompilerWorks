package com.jonheard.compilers.jvmClassBuilder;

/// Bitwise anding signed bytes with 0xff gets their unsigned values by:
/// 1) converting the resulting signed byte to a signed int
/// 2) zeroing all bits in the converted value except the first 8 bits

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.*;

public class MethodCodeBuilderTest
{
	private ConstantPool constantPool;
	private MethodRep rep;
	MethodCodeBuilder builder;
	
	public void setup()
	{
		constantPool = new ConstantPool();
		rep = new MethodRep(
			"name1", "(I)V",
			Arrays.asList("public", "static"),
			constantPool);
		builder = rep.getMethodCodeBuilder();
	}
	
	@Test
	public void basic()
	{
		setup();
		
		assertEquals(rep, builder.getMethodRep());
		assertEquals(1, rep.getLocalSize());
		assertEquals(0, rep.getStackSize());
	}
	
	@Test
	public void addCodeAndLabels()
	{
		setup();
		
		builder.addOp(Op_Label._goto, "finished");
		builder.addOp(Op_NoArg._nop);
		builder.addOp(Op_NoArg._nop);
		builder.addOp(Op_NoArg._nop);
		builder.addLabel("finished");
		builder.addOp(Op_NoArg._return);
		
		assertEquals(1, rep.getLocalSize());
		assertEquals(0, rep.getStackSize());
		
		DataBuffer data = rep.serialize();
		assertEquals(19, data.getInt(10));
		assertEquals(7, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xa7, data.nextByte() & 0xff);
		assertEquals(0x06, data.nextShort());
		assertEquals(0x00, data.nextByte());
		assertEquals(0x00, data.nextByte());
		assertEquals(0x00, data.nextByte());
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xb1, data.nextByte() & 0xff);
	}
	
	@Test
	public void addOps_noArg()
	{
		setup();

		rep.setLocalSize(0);
		builder.addOp(Op_NoArg._nop);
			assertEquals(0, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._dup);
			assertEquals(1, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._return);
			assertEquals(1, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._aload_0);
			assertEquals(2, rep.getStackSize());
			assertEquals(1, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._aload_1);
			assertEquals(3, rep.getStackSize());
			assertEquals(2, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._aload_2);
			assertEquals(4, rep.getStackSize());
			assertEquals(3, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._aload_3);
			assertEquals(5, rep.getStackSize());
			assertEquals(4, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._astore_0);
			assertEquals(5, rep.getStackSize());
			assertEquals(1, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._astore_1);
			assertEquals(5, rep.getStackSize());
			assertEquals(2, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._astore_2);
			assertEquals(5, rep.getStackSize());
			assertEquals(3, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._astore_3);
			assertEquals(5, rep.getStackSize());
			assertEquals(4, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._iconst_0);
			assertEquals(5, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._iconst_1);
			assertEquals(5, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._iconst_2);
			assertEquals(5, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._iconst_3);
			assertEquals(5, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._iconst_4);
			assertEquals(6, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._iconst_5);
			assertEquals(7, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());
		builder.addOp(Op_NoArg._istore_0);
			assertEquals(7, rep.getStackSize());
			assertEquals(1, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._istore_1);
			assertEquals(7, rep.getStackSize());
			assertEquals(2, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._istore_2);
			assertEquals(7, rep.getStackSize());
			assertEquals(3, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._istore_3);
			assertEquals(7, rep.getStackSize());
			assertEquals(4, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._iload_0);
			assertEquals(7, rep.getStackSize());
			assertEquals(1, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._iload_1);
			assertEquals(7, rep.getStackSize());
			assertEquals(2, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._iload_2);
			assertEquals(7, rep.getStackSize());
			assertEquals(3, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._iload_3);
			assertEquals(7, rep.getStackSize());
			assertEquals(4, rep.getLocalSize());
			rep.setLocalSize(0);
		builder.addOp(Op_NoArg._arraylength);
			assertEquals(7, rep.getStackSize());
			assertEquals(0, rep.getLocalSize());

		DataBuffer data = rep.serialize();
		assertEquals(Op_NoArg.values().length + 12, data.getInt(10));
		assertEquals(Op_NoArg.values().length, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0x00, data.nextByte() & 0xff); /// nop
		assertEquals(0x59, data.nextByte() & 0xff); /// dup
		assertEquals(0xb1, data.nextByte() & 0xff); /// return
		assertEquals(0x2a, data.nextByte() & 0xff); /// aload_0
		assertEquals(0x2b, data.nextByte() & 0xff); /// aload_1
		assertEquals(0x2c, data.nextByte() & 0xff); /// aload_2 
		assertEquals(0x2d, data.nextByte() & 0xff); /// aload_3
		assertEquals(0x4b, data.nextByte() & 0xff); /// astore_0
		assertEquals(0x4c, data.nextByte() & 0xff); /// astore_1
		assertEquals(0x4d, data.nextByte() & 0xff); /// astore_2
		assertEquals(0x4e, data.nextByte() & 0xff); /// astore_3
		assertEquals(0x03, data.nextByte() & 0xff); /// iconst_0
		assertEquals(0x04, data.nextByte() & 0xff); /// iconst_1
		assertEquals(0x05, data.nextByte() & 0xff); /// iconst_2
		assertEquals(0x06, data.nextByte() & 0xff); /// iconst_3
		assertEquals(0x07, data.nextByte() & 0xff); /// iconst_4
		assertEquals(0x08, data.nextByte() & 0xff); /// iconst_5
		assertEquals(0x3b, data.nextByte() & 0xff); /// istore_0
		assertEquals(0x3c, data.nextByte() & 0xff); /// istore_1
		assertEquals(0x3d, data.nextByte() & 0xff); /// istore_2
		assertEquals(0x3e, data.nextByte() & 0xff); /// istore_3
		assertEquals(0x1a, data.nextByte() & 0xff); /// iload_0
		assertEquals(0x1b, data.nextByte() & 0xff); /// iload_1
		assertEquals(0x1c, data.nextByte() & 0xff); /// iload_2
		assertEquals(0x1d, data.nextByte() & 0xff); /// iload_3
		assertEquals(0xbe, data.nextByte() & 0xff); /// arraylength
	}

	@Test
	public void addOps_class()
	{
		setup();

		builder.addOp(Op_Class._new, "java/lang/Object");
		assertEquals(1, rep.getStackSize());

		assertEquals(1, rep.getLocalSize());

		DataBuffer data = rep.serialize();
		assertEquals(Op_Class.values().length*3 + 12, data.getInt(10));
		assertEquals(Op_Class.values().length*3, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xbb, data.nextByte() & 0xff); /// new
		short expected = constantPool.addClass("java/lang/Object");
		short actual = data.nextShort();
		assertEquals(expected, actual);
	}

	@Test
	public void addOps_Byte()
	{
		setup();

		builder.addOp(Op_Byte._newarray, 12);
		assertEquals(0, rep.getStackSize());
		builder.addOp(Op_Byte._bipush, 21);
		assertEquals(1, rep.getStackSize());

		assertEquals(1, rep.getLocalSize());
		
		DataBuffer data = rep.serialize();
		assertEquals(Op_Byte.values().length*2 + 12, data.getInt(10));
		assertEquals(Op_Byte.values().length*2, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xbc, data.nextByte() & 0xff); /// newarray
		assertEquals(12, data.nextByte() & 0xff); /// newarray param
		assertEquals(0x10, data.nextByte() & 0xff); /// bipush
		assertEquals(21, data.nextByte() & 0xff); /// bipush param
	}

	@Test
	public void addOps_Label()
	{
		setup();

		builder.addOp(Op_Label._if_icmpeq, "label1");
		builder.addOp(Op_Label._if_icmpne, "label1");
		builder.addOp(Op_Label._if_icmplt, "label1");
		builder.addOp(Op_Label._if_icmple, "label1");
		builder.addLabel("label1");
		builder.addOp(Op_Label._if_icmpgt, "label1");
		builder.addOp(Op_Label._if_icmpge, "label1");
		builder.addOp(Op_Label._goto, "label1");

		builder.adjustStackCounter(12);
		assertEquals(0, rep.getStackSize());
		assertEquals(1, rep.getLocalSize());

		DataBuffer data = rep.serialize();
		assertEquals(Op_Label.values().length*3 + 12, data.getInt(10));
		assertEquals(Op_Label.values().length*3, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0x9f, data.nextByte() & 0xff); /// if_icmpeq
		assertEquals(12, data.nextShort()); /// if_icmpeq data
		assertEquals(0xa0, data.nextByte() & 0xff); /// if_icmpne
		assertEquals(9, data.nextShort()); /// if_icmpne data
		assertEquals(0xa1, data.nextByte() & 0xff); /// if_icmplt
		assertEquals(6, data.nextShort()); /// if_icmplt data
		assertEquals(0xa2, data.nextByte() & 0xff); /// if_icmple
		assertEquals(3, data.nextShort()); /// if_icmple data
		assertEquals(0xa3, data.nextByte() & 0xff); /// if_icmpgt
		assertEquals(0, data.nextShort()); /// if_icmpgt data
		assertEquals(0xa4, data.nextByte() & 0xff); /// if_icmpge
		assertEquals(-3, data.nextShort()); /// if_icmpge data
		assertEquals(0xa7, data.nextByte() & 0xff); /// goto
		assertEquals(-6, data.nextShort()); /// goto data
	}
	
	@Test
	public void addOps_Method()
	{
		setup();

		builder.addOp(Op_Method._invokevirtual,   "c1", "m1", "()I");
			assertEquals(0, rep.getStackSize());
			builder.adjustStackCounter(-builder.getStackCounter());
			rep.setStackSize(0);
		builder.addOp(Op_Method._invokespecial,   "c2", "m2", "()J");
			assertEquals(1, rep.getStackSize());
			builder.adjustStackCounter(-builder.getStackCounter());
			rep.setStackSize(0);
		builder.addOp(Op_Method._invokestatic,    "c3", "m3", "()D");
			assertEquals(2, rep.getStackSize());
			builder.adjustStackCounter(-builder.getStackCounter());
			rep.setStackSize(0);
		builder.addOp(Op_Method._invokeinterface, "c4", "m4", "(I)J");
			assertEquals(0, rep.getStackSize());
			builder.adjustStackCounter(-builder.getStackCounter());
			rep.setStackSize(0);
		builder.addOp(Op_Method._invokedynamic,   "c5", "m5", "()V");
			assertEquals(0, rep.getStackSize());

		assertEquals(1, rep.getLocalSize());

		DataBuffer data = rep.serialize();
		assertEquals(Op_Method.values().length*3 + 12, data.getInt(10));
		assertEquals(Op_Method.values().length*3, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xb6, data.nextByte() & 0xff); /// invokevirtual
		assertEquals(
				constantPool.addMethod("c1", "m1", "()I"), data.nextShort());
		assertEquals(0xb7, data.nextByte() & 0xff); /// invokespecial
		assertEquals(
				constantPool.addMethod("c2", "m2", "()J"), data.nextShort());
		assertEquals(0xb8, data.nextByte() & 0xff); /// invokestatic
		assertEquals(
				constantPool.addMethod("c3", "m3", "()D"), data.nextShort());
		assertEquals(0xb9, data.nextByte() & 0xff); /// invokeinterface
		assertEquals(
				constantPool.addMethod("c4", "m4", "(I)J"), data.nextShort());
		assertEquals(0x0a, data.nextByte() & 0xff); /// invokedynamic
		assertEquals(
				constantPool.addMethod("c5", "m5", "()V"), data.nextShort());
	}

	@Test
	public void addOps_Field()
	{
		setup();

		builder.addOp(Op_Field._getstatic, "c1", "f1", "d1");
		builder.addOp(Op_Field._putstatic, "c2", "f2", "d2");
		builder.addOp(Op_Field._getfield,  "c3", "f3", "d3");
		builder.addOp(Op_Field._putfield,  "c4", "f4", "d4");
		
		DataBuffer data = rep.serialize();
		assertEquals(Op_Field.values().length*3 + 12, data.getInt(10));
		assertEquals(Op_Field.values().length*3, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0xb2, data.nextByte() & 0xff); /// getstatic
		assertEquals(
				constantPool.addField("c1", "f1", "d1"), data.nextShort());
		assertEquals(0xb3, data.nextByte() & 0xff); /// putstatic
		assertEquals(
				constantPool.addField("c2", "f2", "d2"), data.nextShort());
		assertEquals(0xb4, data.nextByte() & 0xff); /// getfield
		assertEquals(
				constantPool.addField("c3", "f3", "d3"), data.nextShort());
		assertEquals(0xb5, data.nextByte() & 0xff); /// putfield
		assertEquals(
				constantPool.addField("c4", "f4", "d4"), data.nextShort());
	}

	@Test
	public void addOps_String()
	{
		setup();

		builder.addOp(Op_String._ldc, "s1");
		
		DataBuffer data = rep.serialize();
		assertEquals(Op_String.values().length*2 + 12, data.getInt(10));
		assertEquals(Op_String.values().length*2, data.getInt(18));
		data.setIterator(22);
		/// See line 3 for an explanation of bitwise anding
		assertEquals(0x12, data.nextByte() & 0xff); /// ldc
		assertEquals(
				constantPool.addString("s1"), data.nextByte());
	}
}
