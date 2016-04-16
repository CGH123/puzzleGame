package com.example.administrator.puzzleGame.game3DModel;

public class Vector2f 
{
	public float x;
	public float y;
	public float mod;
	public Vector2f(float X,float Y)
	{
		x=X;
		y=Y;
		mod=(float) Math.sqrt(x*x+y*y);
	}
	//加法
	public Vector2f add(Vector2f v){
		return new Vector2f(this.x+v.x,this.y+v.y);
	}
	//减法
	public Vector2f minus(Vector2f v){
		return new Vector2f(this.x-v.x,this.y-v.y);
	}
	//乘法
	public float multi(Vector2f v){
		return this.x*v.x+this.y*v.y;
	}
	//与常量相乘
	public Vector2f multiK(float k){
		return new Vector2f(this.x*k,this.y*k);
	}
	//规格化
	public void normalize(){
		x /= mod;
		y /= mod;
	}
}
