package com.funmylife.fml.interfaces.request;

import lombok.Data;

/**
 * 笔记详情请求。
 *
 * <p>projectId 和 noteId 都放在 body，application 层会用 projectId 校验笔记归属。</p>
 */
@Data
public class NoteDetailRequest {

    /** 目标项目 ID。 */
    private Long projectId;

    /** 笔记 ID。 */
    private Long noteId;
}
