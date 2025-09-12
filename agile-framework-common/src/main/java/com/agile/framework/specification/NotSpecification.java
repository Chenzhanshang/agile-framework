package com.agile.framework.specification;

/**
 * not规约, 用于创建一个新规约，为原规约的not条件
 * @date: 2023/5/30 10:24
 * @author: chenzhanshang
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

  private Specification<T> spec1;

  /**
   * 创建一个not条件的规约
   * @param spec1 Specification instance to not.
   */
  public NotSpecification(final Specification<T> spec1) {
    this.spec1 = spec1;
  }

  /**
   * 新规约的满足条件
   */
  @Override
  public boolean isSatisfiedBy(final T t) {
    return !spec1.isSatisfiedBy(t);
  }
}
