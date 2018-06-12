package protmetrics.dao.files.pdb;
	/// <summary>
	/// Summary description for PdbLine.
	/// </summary>
	public class PdbLine
	{
		String[] LineTokens;
		public String Line;
		public PdbLine(String a_line)
		{
			Line=a_line;
			LineTokens=null;
		}
		public PdbLine(String a_line,String[] a_lineTokens)
		{
			Line=a_line;
			LineTokens=a_lineTokens;
		}
//		public PdbLine(String[] a_lineTokens)
//		{
//			LineTokens=a_lineTokens;
//		}
//		public String[] LineTokens
//		{
//			get{return LineTokens;}
//			set{LineTokens=value;}
//		}
        public PdbLine Clone()
		{
			return new PdbLine(this.Line,this.LineTokens);
		}
		public String[] GetLineTokens()
        {
		       return this.LineTokens;
		}

	}