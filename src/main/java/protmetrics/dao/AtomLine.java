package protmetrics.dao;

import protmetrics.utils.BioUtils;

/// <summary>
	/// Summary description for AtomLine.
	/// </summary>
	public class AtomLine extends PdbLine
	{


		public int AminoNumberPos;
		public String Sequence;
	    public ThreeDPoint Location;
		public String AminoNumber;
		public String AminoType;
        //private String[] LineTokens;
		public String AtomType;

//		public AtomLine(String[] a_lineTokens):base(a_lineTokens)
//		{
//			//El numero del aminoacido debe ser el segundo numero en a_lineTokens
//
//			int m_aminoNumberPos=this.GetAminoNumberLocation(a_lineTokens);
//			if(m_aminoNumberPos!=-1)
//			{
//				AminoNumber=Integer.parseInt(a_lineTokens[m_aminoNumberPos]);
//			}
//			else
//			{
//				AminoNumber=-1;
//			}
//
//			//Parece que son los 3 que siguen a AminoNumber
//			double m_X=Double.parseDouble(a_lineTokens[m_aminoNumberPos+1]);
//			double m_Y=Double.parseDouble(a_lineTokens[m_aminoNumberPos+2]);
//			double m_Z=Double.parseDouble(a_lineTokens[m_aminoNumberPos+3]);
//			Location=new ThreeDPoint(m_X,m_Y,m_Z);
//
//			//Hasta ahora no parece haber nada que contradiga que esto esta en a_lineTokens[4] segun Michael
//			AminoType=a_lineTokens[3];
//
//			//Parece estar en la a_lineTokens[2]
//			AtomType=a_lineTokens[2];
//
//			//Basados en que delante del numero de aminoacido o esta la secuencia o el tipo de amino
//			if(m_aminoNumberPos!=-1)
//			{
//				if(BioUtils.IsAminoSequence(a_lineTokens[m_aminoNumberPos-1])==false)
//				{
//					Sequence=a_lineTokens[m_aminoNumberPos-1];
//				}
//				else // Hay una sola secuencia
//				{
//					Sequence="A";
//				}
//
//			}
//
//	}
		public AtomLine(String a_line,String[] a_lineTokens)
		{
                        super(a_line,a_lineTokens);
						//El numero del aminoacido debe ser el segundo numero en a_lineTokens
						this.LineTokens=a_lineTokens;

						//Si no lo encuentra entonces !!!! El CAOS
						AminoNumberPos=this.GetAminoNumberLocation(a_lineTokens);
                        if(AminoNumberPos!=-1)
						{
							AminoNumber=a_lineTokens[AminoNumberPos];
						}
						else
						{
							AminoNumber="~";
						}

						//Parece que son los 3 que siguen a AminoNumber
						double m_X=Double.parseDouble(a_lineTokens[AminoNumberPos+1]);
						double m_Y=Double.parseDouble(a_lineTokens[AminoNumberPos+2]);
						double m_Z=Double.parseDouble(a_lineTokens[AminoNumberPos+3]);
						Location=new ThreeDPoint(m_X,m_Y,m_Z);

						//Hasta ahora no parece haber nada que contradiga que esto esta en a_lineTokens[4] segun Michael
						AminoType=a_lineTokens[3];

						//Parece estar en la a_lineTokens[2]
						AtomType=a_lineTokens[2];

						//Basados en que delante del numero de aminoacido o esta la secuencia o el tipo de amino
						if(AminoNumberPos!=-1)
						{
							if(BioUtils.IsAminoSequence(a_lineTokens[AminoNumberPos-1])==false)
							{
								Sequence=a_lineTokens[AminoNumberPos-1];
							}
							else // Hay una sola secuencia
							{
								Sequence="~";
							}

						}

		}
		public int GetAminoNumberLocation(String[] a_lineTokens)
		{

			int m_result=-1;
			int m_numbersFind=0;
			int m_pos=0;
			if(a_lineTokens==null)
			{
			   System.out.println("ES NULL");
			}
            int m_length=a_lineTokens.length;

			while(m_pos<m_length && m_numbersFind<2 )
			{
				if(BioUtils.IsInteger(a_lineTokens[m_pos]))
				{
					m_numbersFind++;
				}
				if(m_numbersFind==2)
				{
					m_result=m_pos;
				}
				m_pos++;
			}//while
			return m_result;

		}

		public  void UpDatePdbLine()
		{
			String m_result=this.LineTokens[0];
			boolean m_changed=false;
			int m_tlength=LineTokens.length;
			for(int i=1;i<m_tlength;i++)
			{
				//Esto sera BRUTAL
				m_changed=false;
				if(i==2)
				{
					m_result=m_result+"	 "+this.AtomType;
					this.LineTokens[2]=this.AtomType;
					m_changed=true;
				}
				if(i==3)
				{
					m_result=m_result+"	 "+this.AminoType;
					this.LineTokens[3]=this.AminoType;
					m_changed=true;
				}
				if(i==AminoNumberPos-1)
				{
					if(BioUtils.IsAminoSequence(this.LineTokens[AminoNumberPos-1])==false)
					{
						m_result=m_result+"	 "+this.Sequence;
						this.LineTokens[AminoNumberPos-1]=this.Sequence;
						m_changed=true;
					}
					//Notar que si no hay secuencia no se puede cambiar CLARO ESTA!!!!
				}
				if(i==AminoNumberPos+1)
				{
					m_result=m_result+"	 "+this.Location.X;
					this.LineTokens[AminoNumberPos+1]=Double.toString(this.Location.X);
					m_changed=true;
				}
				if(i==AminoNumberPos+2)
				{
					m_result=m_result+"	 "+this.Location.Y;
					this.LineTokens[AminoNumberPos+2]=Double.toString(this.Location.Y);
					m_changed=true;
				}
				if(i==AminoNumberPos+3)
				{
					m_result=m_result+"	 "+this.Location.Z;
					this.LineTokens[AminoNumberPos+3]=Double.toString(this.Location.Z);
					m_changed=true;
				}
				if(m_changed==false)
				{
					m_result=m_result+"	 "+this.LineTokens[i];
				}

			}//for(int i=0;i<m_tlength;i++)
			this.Line= m_result;
		}//public  String UpDateString()

		public  PdbLine Clone()
		{
			AtomLine m_result=new AtomLine(this.Line,this.LineTokens);
			return m_result;
		}
		public int GetAminoNumberPos()
		{
		    return this.AminoNumberPos;
		}
		public void SetAminoNumberPos(int a_newANP)
        {
        	this.AminoNumberPos=a_newANP;
            this.UpDatePdbLine();
        }
		public void SetSequence(String a_newSequence)
		{
		    this.Sequence=a_newSequence;
		    this.UpDatePdbLine();
		}
		public String GetSequence()
		{
		    return this.Sequence;
		}
	    public ThreeDPoint GetLocation()
	    {
	        return this.Location;
	    }
	    public void SetLocation(ThreeDPoint a_newLocation)
	    {
	       this.Location=a_newLocation;
           this.UpDatePdbLine();
	    }
		public void SetAminoNumber(String a_newAN)
		{
		    this.AminoNumber=a_newAN;
		    this.UpDatePdbLine();
		}
		public String GetAminoNumber()
		{
		    return this.AminoNumber;
		}
		public void SetAminoType(String a_newAT)
		{
		    this.AminoType=a_newAT;
		    this.UpDatePdbLine();
		}
		public String GetAminoType()
		{
		    return this.AminoType;
		}
		public String GetAtomType()
		{
		    return this.AtomType;
		}
		public void SetAtomType(String a_newAT)
		{
		  this.AtomType=a_newAT;
          this.UpDatePdbLine();
		}
		 public String[] GetLineTokens()
		 {
		     return this.LineTokens;
		 }


}// AtomLine class