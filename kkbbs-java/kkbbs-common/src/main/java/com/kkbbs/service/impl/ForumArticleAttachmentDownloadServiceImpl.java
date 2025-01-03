package com.kkbbs.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.kkbbs.entity.enums.PageSize;
import com.kkbbs.entity.query.ForumArticleAttachmentDownloadQuery;
import com.kkbbs.entity.po.ForumArticleAttachmentDownload;
import com.kkbbs.entity.vo.PaginationResultVO;
import com.kkbbs.entity.query.SimplePage;
import com.kkbbs.mappers.ForumArticleAttachmentDownloadMapper;
import com.kkbbs.service.ForumArticleAttachmentDownloadService;


/**
 * 
 * 用户附件下载 业务接口实现
 * 
 */
@Service("forumArticleAttachmentDownloadService")
public class ForumArticleAttachmentDownloadServiceImpl implements ForumArticleAttachmentDownloadService {

	@Resource
	private ForumArticleAttachmentDownloadMapper<ForumArticleAttachmentDownload,ForumArticleAttachmentDownloadQuery> forumArticleAttachmentDownloadMapper;

	/**
	 * 根据条件查询列表
	 */
	@Override
	public List<ForumArticleAttachmentDownload> findListByParam(ForumArticleAttachmentDownloadQuery param) {
		return this.forumArticleAttachmentDownloadMapper.selectList(param);
	}

	/**
	 * 根据条件查询列表
	 */
	@Override
	public Integer findCountByParam(ForumArticleAttachmentDownloadQuery param) {
		return this.forumArticleAttachmentDownloadMapper.selectCount(param);
	}

	/**
	 * 分页查询方法
	 */
	@Override
	public PaginationResultVO<ForumArticleAttachmentDownload> findListByPage(ForumArticleAttachmentDownloadQuery param) {
		int count = this.findCountByParam(param);
		int pageSize = param.getPageSize()==null?PageSize.SIZE15.getSize():param.getPageSize();

		SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
		param.setSimplePage(page);
		List<ForumArticleAttachmentDownload> list = this.findListByParam(param);
		PaginationResultVO<ForumArticleAttachmentDownload> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(),page.getPageTotal(), list);
		return result;
	}

	/**
	 * 新增
	 */
	@Override
	public Integer add(ForumArticleAttachmentDownload bean){
		return this.forumArticleAttachmentDownloadMapper.insert(bean);
	}

	/**
	 * 批量新增
	 */
	@Override
	public Integer addBatch(List<ForumArticleAttachmentDownload> listBean){
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleAttachmentDownloadMapper.insertBatch(listBean);
	}

	/**
	 * 批量新增或者修改
	 */
	@Override
	public Integer addOrUpdateBatch(List<ForumArticleAttachmentDownload> listBean){
		if (listBean == null || listBean.isEmpty()) {
			return 0;
		}
		return this.forumArticleAttachmentDownloadMapper.insertOrUpdateBatch(listBean);
	}

	/**
	 * 根据FileIdAndUserId获取对象
	 */
	@Override
	public ForumArticleAttachmentDownload getForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId){
		return this.forumArticleAttachmentDownloadMapper.selectByFileIdAndUserId(fileId,userId);
	}

	/**
	 * 根据FileIdAndUserId修改
	 */
	@Override
	public Integer updateForumArticleAttachmentDownloadByFileIdAndUserId(ForumArticleAttachmentDownload bean,String fileId,String userId){
		return this.forumArticleAttachmentDownloadMapper.updateByFileIdAndUserId(bean,fileId,userId);
	}

	/**
	 * 根据FileIdAndUserId删除
	 */
	@Override
	public Integer deleteForumArticleAttachmentDownloadByFileIdAndUserId(String fileId,String userId){
		return this.forumArticleAttachmentDownloadMapper.deleteByFileIdAndUserId(fileId,userId);
	}
}