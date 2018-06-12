package protmetrics.utils.propertymatrix;
	/// <summary>
	/// Summary description for PropertyVectorElement.
	/// </summary>
	public class PropertyVectorElement
	{

		public int ElementNumber;
		public String ElementSimpleName;
		public String ElementFullName;
		public double ElementValue;

		public PropertyVectorElement(int a_elementNumber,String a_elementSimpleName,String a_elementFullName,double a_elementValue)
		{
			this.ElementNumber=a_elementNumber;
			this.ElementSimpleName=a_elementSimpleName;
			this.ElementFullName=a_elementFullName;
			this.ElementValue=a_elementValue;
		}


		public double GetValueFromNumber(int a_elementNumber,boolean[] o_found)
		{
			if(this.ElementNumber==a_elementNumber)
			{
				o_found[0]=true;
				return this.ElementNumber;
			}
			o_found[0]=false;
			return 0;
		}//public double GetElementFromNumber
		public double GetValueFromName(String a_elementName, boolean[] o_found)
		{
			if((this.ElementSimpleName.equals(a_elementName))||(this.ElementFullName.equals(a_elementName)))
			{
				o_found[0]=true;
				return this.ElementValue;
			}
			o_found[0]=false;
			return 0;
		}//public double GetValueFromName(String a_elementName)

	}