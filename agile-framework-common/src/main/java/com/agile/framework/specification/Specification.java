package com.agile.framework.specification;

/**
 * 规约
 * @date: 2023/5/30 10:18
 * @author: chenzhanshang
 */
public interface Specification<T> {

  /**
   * 是否满足条件
   * @param t 领域对象
   * @return true-满足， false-不满足
   */
  boolean isSatisfiedBy(T t);

  /**
   * 创建一个新的规约，与当前规约共同组成新规约，组成的规约条件必须都满足
   * @param specification 组合的规约
   * @return 新规约
   */
  Specification<T> and(Specification<T> specification);

  /**
   * 创建一个新的规约，与当前规约共同组成新规约，组成的规约条件满足至少一个
   * @param specification 组合的规约
   * @return 新规约
   */
  Specification<T> or(Specification<T> specification);

  /**
   * 基于当前规约创建not规约
   * @return 新规约
   */
  Specification<T> not();
}
