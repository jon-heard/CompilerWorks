package com.jonheard.compilers.assembler_jvm.backEnd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jonheard.util.HelperMethods;

/*
 * MethodCodeBuilder - A class holding functionality to write the code data for
 * a MethodRep object.
 */
public class MethodCodeBuilder
{
	private int stackCounter = 0;
	private MethodRep methodRep;
	private ConstantPool constantPool;
	private DataBuffer codeData;
	private HashMap<String, Integer> labels = new HashMap<String, Integer>();
	/// A label referenced before being defined is stored with indexes to their
	/// references.  Once the label is defined, all of the indexes to that
	/// label are updated to the label position.
	private HashMap<String, List<Integer>> preLabels =
			new HashMap<String, List<Integer>>();
	
	protected MethodCodeBuilder(
			MethodRep methodRep, DataBuffer codeData, ConstantPool constantPool)
	{
		this.methodRep = methodRep;
		this.codeData = codeData;
		this.constantPool = constantPool;
		
		/// Setup the initial local size based on the method's argument count
		int initialLocalSize = HelperMethods.getStackSizeOfMethodDescriptor(
				methodRep.getDescriptor());
		if((methodRep.getModifiers()&0x0008) == 0) /// Non static method
		{
			initialLocalSize++; /// Add 1 to localSize for the 'this' value
		}
		padLocalSize(initialLocalSize);
	}
	
	public MethodRep getMethodRep()
	{
		return methodRep;
	}
	
	/// Called when referencing the 'locals' stack area as evidence of the
	/// 'locals' stack size
	public void padLocalSize(int potentialSize)
	{
		if(methodRep.getLocalSize() < potentialSize)
		{
			methodRep.setLocalSize(potentialSize);
		}
	}
	/// Called each time an added op would push or pop the jvm stack
	public void adjustStackCounter(int amount)
	{
		stackCounter += amount;
		if(stackCounter > methodRep.getStackSize())
		{
			methodRep.setStackSize(stackCounter);
		}
	}
	/// Checked to make sure code ends at an empty stack
	public int getStackCounter()
	{
		return stackCounter;
	}
	public void resetStackAndLocalCounts()
	{
		stackCounter = 0;
		methodRep.setStackSize(0);
		methodRep.setLocalSize(0);
	}
	
	
	public void addLabel(String label)
	{
		int labelAddress = codeData.size();
		labels.put(label, labelAddress);
		if(preLabels.containsKey(label))
		{
			for(Integer labelRef : preLabels.get(label))
			{
				codeData.setShort(labelRef, (short)(labelAddress-labelRef+1));
			}
			preLabels.remove(label);
		}
	}
	
	public void addOp(Op_NoArg op)
	{
		codeData.add(op.getOpcode());
		if(
				op == Op_NoArg._aload_0 || op == Op_NoArg._astore_0 ||
				op == Op_NoArg._istore_0 || op == Op_NoArg._iload_0)
			padLocalSize(1);
		if(
				op == Op_NoArg._aload_1 || op == Op_NoArg._astore_1 ||
				op == Op_NoArg._istore_1 || op == Op_NoArg._iload_1)
			padLocalSize(2);
		if(
				op == Op_NoArg._aload_2 || op == Op_NoArg._astore_2 ||
				op == Op_NoArg._istore_2 || op == Op_NoArg._iload_2)
			padLocalSize(3);
		if(
				op == Op_NoArg._aload_3 || op == Op_NoArg._astore_3 ||
				op == Op_NoArg._istore_3 || op == Op_NoArg._iload_3)
			padLocalSize(4);
		adjustStackCounter(op.getStackAdjust());
	}
	
	public void addOp(Op_Class op, String className)
	{
		codeData.add(op.getOpcode());
		short index = constantPool.addClass(className);
		codeData.add(index);
		adjustStackCounter(op.getStackAdjust());
	}
	
	public void addOp(Op_Byte op, int number)
	{
		codeData.add(op.getOpcode());
		codeData.add((byte)number);
		adjustStackCounter(op.getStackAdjust());
	}
	
	public void addOp(Op_Label op, String label)
	{
		codeData.add(op.getOpcode());
		if(labels.containsKey(label))
		{
			int offset = labels.get(label) - codeData.size() + 1;
			codeData.add((short)offset);
		}
		else
		{
			if(!preLabels.containsKey(label))
			{
				preLabels.put(label, new ArrayList<Integer>());
			}
			preLabels.get(label).add(codeData.size());
			codeData.add((short)0);
		}
		adjustStackCounter(op.getStackAdjust());
	}
	
	public void addOp(Op_Method op,
			String className, String methodName, String descriptor)
	{
		codeData.add(op.getOpcode());
		short methodIndex = constantPool.addMethod(
				className, methodName, descriptor);
		codeData.add(methodIndex);
		if(		op == Op_Method._invokedynamic ||
				op == Op_Method._invokeinterface ||
				op == Op_Method._invokevirtual ||
				op == Op_Method._invokespecial)
		{
			String retDescriptor = descriptor.substring(
					descriptor.indexOf(')') + 1);
			adjustStackCounter(-1 +
					HelperMethods.getStackSizeOfFieldDescriptor(retDescriptor) -
					HelperMethods.getStackSizeOfMethodDescriptor(descriptor));
		}
		else if(op == Op_Method._invokestatic)
		{
			String retDescriptor = descriptor.substring(
					descriptor.indexOf(')') + 1);
			adjustStackCounter(
					HelperMethods.getStackSizeOfFieldDescriptor(retDescriptor) -
					HelperMethods.getStackSizeOfMethodDescriptor(descriptor));
		}
		else
		{
			adjustStackCounter(op.getStackAdjust());
		}
	}
	
	public void addOp(Op_Field op,
			String className, String fieldName, String descriptor)
	{
		codeData.add(op.getOpcode());
		short fieldIndex =
				constantPool.addField(className, fieldName, descriptor);
		codeData.add(fieldIndex);
		adjustStackCounter(op.getStackAdjust());
	}
	
	public void addOp(Op_String op, String value)
	{
		short index = constantPool.addString(value);
		if(op == Op_String._ldc)
		{
			if(index < 256)
			{
				codeData.add(op.getOpcode());
				codeData.add((byte)index);
			}
			else
			{
				codeData.add(op.getOpcode()+1);
				codeData.add(index);
			}
		}
		adjustStackCounter(op.getStackAdjust());
	}
	
	

	
	/*
	 *  The following enums store all of the operations available in the jvm.
	 *  They are used by the 'MethodBilder' class to allow the user to add code.
	 */

	/// Each operation has an amount it pushes or pops the stack (stackAdjust).
	/// Some operations adjust the stack size a variable amount based on their
	/// parameters.  The stackAdjust for those ops is set CUSTOM_STACK_ADJUST.
	static final int CUSTOM_STACK_ADJUST = Integer.MAX_VALUE;
	
	public interface Op
	{
		byte getOpcode();
		int getStackAdjust();
	}

	public enum Op_NoArg implements Op
	{
		_nop(0x0, 0),
		_dup(0x59, 1),
		_return(0xb1, 0),
		_aload_0(0x2a, 1),
		_aload_1(0x2b, 1),
		_aload_2(0x2c, 1),
		_aload_3(0x2d, 1),
		_astore_0(0x4b, -1),
		_astore_1(0x4c, -1),
		_astore_2(0x4d, -1),
		_astore_3(0x4e, -1),
		_iconst_0(0x03, 1),
		_iconst_1(0x04, 1),
		_iconst_2(0x05, 1),
		_iconst_3(0x06, 1),
		_iconst_4(0x07, 1),
		_iconst_5(0x08, 1),
		_istore_0(0x3b, -1),
		_istore_1(0x3c, -1),
		_istore_2(0x3d, -1),
		_istore_3(0x3e, -1),
		_iload_0(0x1a, 1),
		_iload_1(0x1b, 1),
		_iload_2(0x1c, 1),
		_iload_3(0x1d, 1),		
		_arraylength(0xbe, 0);
		private byte opcode;
		private int stackAdjust;
		private Op_NoArg(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}
	
	public enum Op_Class implements Op
	{
		_new(0xbb, 1);
		private byte opcode;
		private int stackAdjust;
		private Op_Class(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}
	
	public enum Op_Byte implements Op
	{
		_newarray(0xbc, 0),
		_bipush(0x10, 1);
		private byte opcode;
		private int stackAdjust;
		private Op_Byte(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}
	
	public enum Op_Label implements Op
	{
		_if_icmpeq(0x9f, -2),
		_if_icmpne(0xa0, -2),
		_if_icmplt(0xa1, -2),
		_if_icmple(0xa2, -2),
		_if_icmpgt(0xa3, -2),
		_if_icmpge(0xa4, -2),
		_goto(0xa7, 0);
		
		private byte opcode;
		private int stackAdjust;
		private Op_Label(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}
	
	public enum Op_Method implements Op
	{
		_invokevirtual(0xb6, CUSTOM_STACK_ADJUST),
		_invokespecial(0xb7, CUSTOM_STACK_ADJUST),
		_invokestatic(0xb8, CUSTOM_STACK_ADJUST),
		_invokeinterface(0xb9, CUSTOM_STACK_ADJUST),
		_invokedynamic(0x0a, CUSTOM_STACK_ADJUST);
		private byte opcode;
		private int stackAdjust;
		private Op_Method(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}

	public enum Op_Field implements Op
	{
		_getstatic(0xb2, 1),
		_putstatic(0xb3, -1),
		_getfield(0xb4, 0),
		_putfield(0xb5, -2);
		private byte opcode;
		private int stackAdjust;
		private Op_Field(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}

	public enum Op_String implements Op
	{
		_ldc(0x12, 1);
		private byte opcode;
		private int stackAdjust;
		private Op_String(int opcode, int stackAdjust)
		{
			this.opcode = (byte)opcode;
			this.stackAdjust = stackAdjust;
		}
		public byte getOpcode() { return opcode; }
		public int getStackAdjust() { return stackAdjust; }
	}
}
