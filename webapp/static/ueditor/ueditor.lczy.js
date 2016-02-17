/*
action类型以及说明
    uploadimage：//执行上传图片或截图的action名称
    uploadscrawl：//执行上传涂鸦的action名称
    uploadvideo：//执行上传视频的action名称
    uploadfile：//controller里,执行上传视频的action名称
    catchimage：//执行抓取远程图片的action名称
    listimage：//执行列出图片的action名称
    listfile：//执行列出文件的action名称
*/
UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadfile' || action == 'uploadvideo') {
        return ctx + '/ueditor/upload?ajax&action='+action;
    } else if (action == 'catchimage') {
        return ctx + '/ueditor/catchimage?ajax&action='+action;
    } else {
        return this._bkGetActionUrl.call(this, action);
    }
}