package com.agile.framework.specification;


/**
 * 规约抽象类，实现规约AND OR NOT逻辑实现
 * @date: 2023/5/30 10:32
 * @author: chenzhanshang
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

  /**
   * 规约条件
   */
  @Override
  public abstract boolean isSatisfiedBy(T t);

  /**
   * 基于当前规约创建AND规约
   */
  @Override
  public Specification<T> and(final Specification<T> specification) {
    return new AndSpecification<T>(this, specification);
  }

  /**
   * 基于当前规约创建OR规约
   */
  @Override
  public Specification<T> or(final Specification<T> specification) {
    return new OrSpecification<T>(this, specification);
  }

  /**
   * 基于当前规约创建not规约
   */
  @Override
  public Specification<T> not() {
    return new NotSpecification<T>(this);
  }
}
