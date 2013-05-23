在制做一个软件的时候要用到侧滑菜单（SlidMenu），而且，现在这个应用在软件开发中应用很多！虽然网上有好多说是实现了这个功能，csdn上资源下载那里也有好多提供下载，但当我一个一个的测试了之后，只能感叹一句“坑爹啊！”都是由bug的，想想还是自己来实现吧！

实现上主要就是一个自定义的MySlidView，在这个MySlidView里边去加载两个你要显示的View（mMenuView， mSlidView），即一个是滑动之后，左侧的mSlidView，另一个就是关闭mMenuView之后的mSlidView！

而菜单的打开与关闭，实际上就是操作上层的mSlidView的滑动，当需要打开菜单时，让mSlidView向右边滑动，如果要关闭menu，就再让它滑回来!

![image](http://image142-c.poco.cn/mypoco/myphoto/20130523/17/6478137120130523170141058.png)
