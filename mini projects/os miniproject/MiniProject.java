package os;

import java.awt.Graphics;

import java.awt.GridLayout;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.*;
import java.awt.Container;


 class Project
{
	int burst,index,burst_left,departure=0,wait=0,turnaround=0;
	boolean flag=true;
	public static int avg_wait=0,avg_turnaround=0,sum,quantum;
	public Project(){}
	public Project(int i,int b)
	{
		burst=burst_left=b;
		index=i;
	}
	public String toString()
	{
		return index+"\t"+burst+"\t"+wait+"\t"+turnaround;
	}
	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter the number of locations to visit in memory:\t");
		int m=sc.nextInt();
		Project a[][]=new Project[m][];
		Vector<Gaant> vector[] = new Vector[m];
		Vector<Integer> starting =new Vector();
		
		int locations[]=new int[m+1];
		boolean visited[]=new boolean[m+1];
		int sub[]=new int[m+1];
		System.out.println("Enter current Location:");
		visited[0]=true;
		locations[0]=sc.nextInt();
		starting.add((locations[0]));
		sub[0]=9999;
		
		for(int i=1;i<m+1;i++)
		{
			System.out.println("Enter location "+i);
			locations[i]=sc.nextInt();
			sub[i]=Math.abs(locations[i]-locations[0]);
		}
		for(int l=0;l<m;l++)	
		{
			int min=9999;
			int next=-1;
			for(int k=1;k<m+1;k++)
			{
				if(!visited[k]&&min>sub[k])
				{
					min=sub[k];
					next=k;
				}
			}
			visited[next]=true;
			starting.add(locations[next]);
			System.out.println("Enter the number of processes :\t" +" at location "+locations[next]);
			int n=sc.nextInt();
			a[l]=new Project[n];
			for(int i=0;i<n;i++)
			{
				System.out.println("Enter the burst time for process "+(i+1));
				a[l][i]=new Project(i+1,sc.nextInt());
			}
			if(n!=0)
			{
				System.out.println("Enter the quantum time slice:\t");
				quantum=sc.nextInt();
				vector[l] = new Vector<Gaant>();
			}
			int count=n;
			sum=0;
			while(count>0)
			{
			/*	System.out.println("SUM:\t"+sum);*/
				for(int i=0;i<n;i++)
				{
					if(a[l][i].flag)
					{
						if(a[l][i].burst_left<=quantum)
						{
							a[l][i].wait+=sum-a[l][i].departure;
							sum+=a[l][i].burst_left;
							vector[l].add(new Gaant(sum-a[l][i].burst_left,a[l][i].index,a[l][i].burst_left));
							a[l][i].burst_left=0;
							a[l][i].departure=sum;
							a[l][i].flag=false;
							count--;
						}
						else
						{
							a[l][i].wait+=sum-a[l][i].departure;
							sum+=quantum;
							vector[l].add(new Gaant(sum-quantum,a[l][i].index,quantum));
							a[l][i].departure=sum;
							a[l][i].burst_left-=quantum;
						}
					}
				}
			}
			for(int i=0;i<n;i++)
			{
				a[l][i].turnaround=a[l][i].burst+a[l][i].wait;
				avg_wait+=a[l][i].wait;
				avg_turnaround+=a[l][i].turnaround;
			/*	System.out.println(a[l][i]);*/
			}
/*			System.out.println("Index\tburst\twait\ttat\n");
			System.out.println("Average wait:\t"+(avg_wait*1.0/n)+"\nAverage TurnAround:\t"+(avg_turnaround*1.0/n));*/
			
		}
		System.out.print("SSTF:\t");
		for(int i=0;i<m;i++)
			System.out.print(starting.get(i)+"\t");
			new DrawAnimation(vector,starting);
	}
}

class Gaant
{
	int arrival , index , execution;
	Gaant(int a,int i,int e)
	{
		arrival=a;
		index=i;
		execution=e;
	}
	int getA()
	{
		return arrival;
	}
	int getI()
	{
		return index;
	}
	int getE()
	{
		return execution;
	}
}
class DrawAnimation extends JFrame
{
	Vector<Gaant> v[];
	Vector<Integer> starting;
	public DrawAnimation(Vector<Gaant> vector[],Vector<Integer> start)
	{
		super("Scheduling");
		v=vector;
		starting=start;
		java.awt.Container con=getContentPane();
		setSize(1000,1000);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void paint(Graphics g)
	{
		g.drawLine(40,100, 890, 100);
		for(int i=0;i<starting.size();i++)
		{
			g.drawString(starting.get(i).toString(),starting.get(i)-5,90);
			g.drawLine(starting.get(i), 100, starting.get(i), 80);
		}
		for(int i=0;i<starting.size()-1;i++)
		{
			g.fillOval(starting.get(i),140+30*(i+1), 5, 5);
			g.drawLine(starting.get(i),140+30*(i+1), starting.get(i+1), 140+30*(i+2));
		}
		g.fillOval(starting.get(starting.size()-1),140+30*(starting.size()), 5, 5);
		
		int x,y=75;
		for(int k=0;k<v.length;k++)
		{
			y+=20*(k+1);
			x=starting.get(k+1);
			for(int j=0;j<v[k].size();j++)
			{
				int width = 10,height = 16;
				Gaant obj=v[k].get(j);
				width+=10*obj.getE();
				x=starting.get(k+1)+obj.getA()*10;
				//System.out.println("x: "+x+"\ty: "+y+"\twidth: "+(10*obj.getA()));
				g.drawRect(x, y, width, height);
				g.drawString("P"+obj.getI(),x+width/2-5, y+10);
				g.drawString(""+obj.getE(),x+width/2-5, y+30);
			}
		}
	}
}
	