package com.agile.framework.specification;

/**
 * OR规约, 用于创建一个新规约，为两个规约的OR条件
 * @date: 2023/5/30 10:20
 * @author: chenzhanshang
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

  private Specification<T> spec1;
  private Specification<T> spec2;

  /**
   * 基于两个规约创建一个新的OR规约
   * @param spec1 Specification one.
   * @param spec2 Specification two.
   */
  public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
    this.spec1 = spec1;
    this.spec2 = spec2;
  }

  /**
   * 新规约的满足条件
   */
  @Override
  public boolean isSatisfiedBy(final T t) {
    return spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);
  }
}
