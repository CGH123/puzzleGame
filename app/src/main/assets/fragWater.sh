precision mediump float;
uniform float swidth;	//设备屏幕的宽度
uniform float sheight;	//设备屏幕的高度
uniform sampler2D sTextureDY;//倒影纹理内容数据
uniform sampler2D sTextureWater;//水面自己纹理内容数据
uniform mat4 uVPMatrix; //摄像机观察及投影的总变换矩阵
varying vec2 vTextureCoord; //接收从顶点着色器过来的参数
void main(){
   //生成对含有倒影的纹理图采样用的纹理坐标
   vec2 tempTexCoor=vec2(gl_FragCoord.x/swidth,gl_FragCoord.y/sheight);
   //进行倒影纹理采样
   vec4 dyColor=texture2D(sTextureDY,tempTexCoor); 
   //进行水自身纹理采样
   vec4 waterColor=texture2D(sTextureWater,vTextureCoord); 
   //混合倒影与水自身得到此片元的最终颜色值
   gl_FragColor =mix(waterColor,dyColor,0.5);     
}              
        